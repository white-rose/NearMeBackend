package com.SmallTalk.AccountHandler;

import com.SmallTalk.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;


    public List<User> pullNearbyUsers() {

        return accountRepository.findAll();

    }

    public List<User> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

}
