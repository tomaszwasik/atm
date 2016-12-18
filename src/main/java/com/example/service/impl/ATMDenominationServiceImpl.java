package com.example.service.impl;

import com.example.model.ATMDenomination;
import com.example.repository.ATMDenominationRepository;
import com.example.service.ATMDenominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Service
public class ATMDenominationServiceImpl implements ATMDenominationService {

    @Autowired
    private ATMDenominationRepository atmDenominationRepository;


    @Override
    public List<ATMDenomination> findATMDenominationsByAccountId(Long accountId) {
        return atmDenominationRepository.findByAccountId(accountId);
    }
}
