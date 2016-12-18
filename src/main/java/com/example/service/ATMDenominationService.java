package com.example.service;

import com.example.model.ATMDenomination;

import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public interface ATMDenominationService {

    List<ATMDenomination> findATMDenominationsByAccountId(Long accountId);
}
