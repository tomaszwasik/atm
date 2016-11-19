package com.example.service.impl;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account findAccountById(Long id) {

        return accountRepository.findOne(id);
    }
}
