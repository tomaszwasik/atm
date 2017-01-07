package com.example.controller;

import com.example.bus.CashPaymentForm;
import com.example.bus.SessionConfigurationModel;
import com.example.model.ATMDenomination;
import com.example.model.Account;
import com.example.model.History;
import com.example.repository.HistoryRepository;
import com.example.service.ATMDenominationService;
import com.example.service.AccountService;
import com.example.util.RedirectionHelper;
import com.example.util.AtmApplicationStatics;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/cash-payment")
public class CashPaymentController {

    @Autowired
    private ATMDenominationService atmDenominationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private HistoryRepository historyRepository;

    @GetMapping
    public String showPage(HttpServletRequest request, Model model) {

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        // minimalny nominał możliwy do wypłaceniam Double.MAX_VALUE jak poszlo cos nie tak
        double minAtmDenomination = findMinAtmDenomination(request);

        if(!validateAtmBalance(request) || minAtmDenomination == Double.MAX_VALUE){
            model.addAttribute("disabledAllFields", true);
            return showPageWithError(request, model, "Wypłata gotówki jest niemożliwa.");
        }

        model.addAttribute("denominationInfo", "Minimalna kwota wypłaty " + minAtmDenomination + " zł.");

        model.addAttribute(AtmApplicationStatics.CASH_PAYMENT_FORM_KEY, new CashPaymentForm());

        return "cash-payment";
    }

    private String showPageWithError(HttpServletRequest request, Model model, String errorMessage){
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(AtmApplicationStatics.CASH_PAYMENT_FORM_KEY, new CashPaymentForm());

        if(StringUtils.isNotBlank(errorMessage)){
            model.addAttribute(AtmApplicationStatics.ERROR_MESSAGE_KEY, errorMessage);
        }

        return "cash-payment";
    }

    @PostMapping("/withdraw-cash")
    public String showCashPaymentPage(@ModelAttribute CashPaymentForm cashPaymentForm, HttpServletRequest request, Model model){

        //==============================================================================================================
        List<Integer[]> withdrawalVariationss = validateATMCashWithdrawalAvailability(request, cashPaymentForm);
        if(CollectionUtils.isEmpty(withdrawalVariationss)){
            return showPageWithError(request, model, "Wypłata nie może być zrealizowana. Proszę wprowadzić inną kwotę.");
        }
        doMagic(request, cashPaymentForm, withdrawalVariationss);
        //==============================================================================================================

        validateATMCashWithdrawalAvailability(request, cashPaymentForm);

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        double minAtmDenomination = findMinAtmDenomination(request);
        if(!validateAtmBalance(request) || minAtmDenomination == Double.MAX_VALUE){
            return showPageWithError(request, model, "Wypłata gotówki jest niemożliwa.");
        }

        // 01 Sprawdzenie czy formularz jest poprawny.
        String validate01 = validateCashPaymentFormFields(request, cashPaymentForm);
        if(!"OK".equals(validate01)){
            return showPageWithError(request, model, validate01);
        }
        
        // 02 Sprawdzenie poprawnosci kwoty w formularzu, czy jest wielokrotnościa najmniejszego nominału itd.
        String validate02 = validateCashPaymentFormAmount(request, cashPaymentForm);
        if(!"OK".equals(validate02)){
            return showPageWithError(request, model, validate02);
        }

        // 03 Sprawdzenie limitów - samej karty jak również konta
        String validate03 = validateCustomerLimits(request, cashPaymentForm);
        if(!"OK".equals(validate03)){
            return showPageWithError(request, model, validate02);
        }

        // 04 Sprawdzenie czy bankomant może prawidłowo wypłacić gotówkę - odpowiednie nominały itd
        List<Integer[]> withdrawalVariations = validateATMCashWithdrawalAvailability(request, cashPaymentForm);
        if(CollectionUtils.isEmpty(withdrawalVariations)){
            return showPageWithError(request, model, "Wypłata nie może być zrealizowana. Proszę wprowadzić inną kwotę.");
        }

        // 05 no to tutaj chyba trzeba odjebac magię wypłaty na bazie
        doMagic(request, cashPaymentForm, withdrawalVariations);



        return RedirectionHelper.showControllerPage(LogoutController.class);
    }

    /**
     * 01
     */
    private String validateCashPaymentFormFields(HttpServletRequest request, CashPaymentForm cashPaymentForm){
        if(cashPaymentForm == null
                || StringUtils.isBlank(cashPaymentForm.getAmount())
                || !StringUtils.isNumeric(cashPaymentForm.getAmount())
                || Long.valueOf(cashPaymentForm.getAmount()).longValue() == 0L){
            return AtmApplicationStatics.DEFAULT_DATA_ERROR_MSG;
        }else if(Long.valueOf(cashPaymentForm.getAmount()).longValue() >= 5000L){
            return "Wprowadzona kwota jest nieprawidłowa. Maksymalna kwota jednorazowej wypłaty to 5000 zł.";
        }
        return "OK";
    }

    /**
     * 02
     */
    private String validateCashPaymentFormAmount(HttpServletRequest request, CashPaymentForm cashPaymentForm){
        double minAtmDenomination = findMinAtmDenomination(request);
        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        Account atmAccount = accountService.findAccountById(sessionConfigurationModel.getAtmAccountId());

        if(minAtmDenomination == Double.MAX_VALUE){
            return "Wypłata gotówki jest niemożliwa.";
        } else if(Double.valueOf(cashPaymentForm.getAmount()).doubleValue() % minAtmDenomination != Double.valueOf(0).doubleValue()){
            return "Wprowadzona kwota jest nieprawidłowa. Bankomant wypłaca wielokrotność " + minAtmDenomination + " zł.";
        }else if(Double.valueOf(cashPaymentForm.getAmount()).doubleValue() > atmAccount.getAtmSaldo().doubleValue()){
            return "Wypłata nie może być zrealizowana. Proszę wprowadzić mniejszą kwotę.";
        }
        return "OK";
    }

    /**
     * 03
     */
    private String validateCustomerLimits(HttpServletRequest request, CashPaymentForm cashPaymentForm){
        return "OK";
    }

    /**
     * 04 zwraca listę z tablicami dostępnych wariacji wypłaty
     */
    private List<Integer[]> validateATMCashWithdrawalAvailability(HttpServletRequest request, CashPaymentForm cashPaymentForm){
        int amountToWitdraw = Integer.valueOf(cashPaymentForm.getAmount());

        // pobrać mapę nominał na ilość w danym bankomacie
        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        List<ATMDenomination> atmDenominationByAccountId = atmDenominationService.findATMDenominationsByAccountId(sessionConfigurationModel.getAtmAccountId());

        // all denominations, amount
        Map<Double, Integer> allDenominations = new HashMap<>();
        for(ATMDenomination atmDenomination : atmDenominationByAccountId){
            allDenominations.put(atmDenomination.getDenominationCurrency().getDenomination().getDenomination().doubleValue(),
                    atmDenomination.getQuantity());
        }

        // posortowana mapa nominałów
        Map<Double, Integer> sortedDenominations = new HashMap<>();

        // posortujemy po kluczu, tak aby nominały były od najmniejszego do największego
        allDenominations.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedDenominations.put(x.getKey(), x.getValue()));


        // tutaj dzieje się magia, głownie dzięki http://stackoverflow.com/questions/22128759/atm-algorithm-of-giving-money-with-limited-amount-of-bank-notes

        int[] values = new int[sortedDenominations.size()];
        int[] amounts = new int[values.length];

        for(int i = 0; i < sortedDenominations.size(); i++){
            Double key = (Double) sortedDenominations.keySet().toArray()[i];
            values[i] = key.intValue();
            amounts[i] = (Integer) sortedDenominations.values().toArray()[i];
        }

        List<Integer[]> results = solutions(values, amounts, new int[5], amountToWitdraw, 0);

        return results;
    }

    private void doMagic(HttpServletRequest request, CashPaymentForm cashPaymentForm, List<Integer[]> withdrawalVariations){
        String amountToWithdraw = cashPaymentForm.getAmount();
        SessionConfigurationModel attribute = (SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        Account userAccount = accountService.findAccountById(attribute.getUserAccountId());
        Account atmAccount = accountService.findAccountById(attribute.getAtmAccountId());
        List<ATMDenomination> atmDenominations = atmDenominationService.findATMDenominationsByAccountId(attribute.getAtmAccountId());


        History history1 = new History();
        history1.setAccountFrom(userAccount);
        history1.setAccountTo(atmAccount);
        history1.setTypeOfTransaction("CASH_WITHDRAWAL");
        History save = historyRepository.save(history1);
        Long id = save.getId();
        System.out.println(id);



        // odjęcie z konta użytkownika
        BigDecimal currentMoney = userAccount.getCurrentMoney();
        currentMoney.subtract(new BigDecimal(amountToWithdraw));

        // aktualizacja limitów
        // TODO: 06.01.2017

        // wybranie wariacji nominałów do wypłaty i odjęcie ich na bankomacie
        Map<Double, Integer> allDenominations = new HashMap<>();
        for(ATMDenomination atmDenomination : atmDenominations){
            allDenominations.put(atmDenomination.getDenominationCurrency().getDenomination().getDenomination().doubleValue(),
                    atmDenomination.getQuantity());
        }

        Map<Double, Integer> sortedDenominations = new HashMap<>();
        allDenominations.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedDenominations.put(x.getKey(), x.getValue()));

        Integer[] finalDenominationVariation = new Integer[sortedDenominations.size()];
        int finalDenominationsCount = Integer.MAX_VALUE;

        for(Integer[] variation : withdrawalVariations){
            int denominationsCount = 0;
            for(int i = 0; i < variation.length; i++){
                denominationsCount = denominationsCount + variation[i].intValue();
            }
            if(denominationsCount < finalDenominationsCount){
                finalDenominationsCount = denominationsCount;
                for(int i = 0; i < variation.length; i++){
                    finalDenominationVariation[i] = variation[i];
                }
            }
        }

        Map<Double, Integer> finalDenominationVariationMap = new HashMap<>();

        for(int i = 0; i < finalDenominationVariation.length; i++){
            finalDenominationVariationMap.put((Double)sortedDenominations.keySet().toArray()[i], finalDenominationVariation[i]);
        }

        for(ATMDenomination denomination : atmDenominations){
            int denominationValue = denomination.getDenominationCurrency().getDenomination().getDenomination().intValue();
            Integer denominationAmount = finalDenominationVariationMap.get(denominationValue);
            int oldQuantity = denomination.getQuantity();
            denomination.setQuantity(oldQuantity - denominationAmount);
        }

        // pomniejszenie kwoty w bankomacie
        BigDecimal atmSaldo = atmAccount.getAtmSaldo();
        atmSaldo.subtract(new BigDecimal(amountToWithdraw));

        // zapisanie operacji w histori
        History history = new History();
        history.setAccountFrom(userAccount);
        history.setAccountTo(atmAccount);
        history.setTypeOfTransaction("CASH_WITHDRAWAL");
        //history.setDateOfTransaction(LocalDateTime.now());


        // todo mozna zrobic history na pin change


    }

    private List<Integer[]> solutions(int[] values, int[] ammounts, int[] variation, int price, int position){
        List<Integer[]> list = new ArrayList<>();
        int value = compute(values, variation);
        if (value < price){
            for (int i = position; i < values.length; i++) {
                if (ammounts[i] > variation[i]){
                    int[] newvariation = variation.clone();
                    newvariation[i]++;
                    List<Integer[]> newList = solutions(values, ammounts, newvariation, price, i);
                    if (newList != null){
                        list.addAll(newList);
                    }
                }
            }
        } else if (value == price) {
            list.add(myCopy(variation));
        }
        return list;
    }
    private int compute(int[] values, int[] variation){
        int ret = 0;
        for (int i = 0; i < variation.length; i++) {
            ret += values[i] * variation[i];
        }
        return ret;
    }

    private Integer[] myCopy(int[] ar){
        Integer[] ret = new Integer[ar.length];
        for (int i = 0; i < ar.length; i++) {
            ret[i] = ar[i];
        }
        return ret;
    }


    private boolean validateAtmBalance(HttpServletRequest request){

        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        // obiekt bankomantu
        Account atmAccount = accountService.findAccountById(sessionConfigurationModel.getAtmAccountId());

        // sprawdzenie bilansu
        BigDecimal atmSaldo = atmAccount.getAtmSaldo();
        if(atmSaldo == null || atmSaldo.longValue() == 0L){
            return false;
        }
        return true;
    }

    /**
     * Sprawdzenie najmniejszego nominału w bankomacie spełniającego warunki ilościowe.
     * @param request
     * @return
     */
    private double findMinAtmDenomination(HttpServletRequest request){
        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);

        // sprawdzenie min nominału
        double minDenomination = Double.MAX_VALUE;

        List<ATMDenomination> atmDenominationByAccountId = atmDenominationService.findATMDenominationsByAccountId(sessionConfigurationModel.getAtmAccountId());

        for(ATMDenomination atmDenomination : atmDenominationByAccountId){

            double denomination = atmDenomination.getDenominationCurrency().getDenomination().getDenomination().doubleValue();

            if(denomination <= 20){
                if(atmDenomination.getQuantity() >= 20 && minDenomination > denomination){
                    minDenomination = Double.valueOf(denomination);
                }
            }else{
                if(atmDenomination.getQuantity() >= 10 && minDenomination > denomination){
                    minDenomination = Double.valueOf(denomination);
                }
            }
        }
        return minDenomination;
    }
}















