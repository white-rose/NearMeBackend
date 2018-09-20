package com.SmallTalk.AccountHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;


    public List pullNearbyUsers() {

        return accountRepository.findAll();

    }

}
