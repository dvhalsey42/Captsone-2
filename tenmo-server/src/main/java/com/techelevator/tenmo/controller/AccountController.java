package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;


    public AccountController(AccountDao accountDao) {
       this.accountDao = accountDao;
    }

    @GetMapping(path = "balance/{userId}")
    public BigDecimal getBalanceById(@PathVariable int userId) {
        return accountDao.getBalanceById(userId);
    }

    @GetMapping(path = "/account")
    public Account getAccountById(@RequestParam(defaultValue = "0") int user_id, @RequestParam(defaultValue = "0") int account_id) {
        if (user_id > 0) {
        return accountDao.getAccountByUserId(user_id);
        } else if(account_id > 0){
            return accountDao.getAccountByAccountId(account_id);
        } else {
            return null;
        }
    }

    @GetMapping(path = "/account/{accountId}")
    public Account getAccountById(@PathVariable int accountId) {
        return accountDao.getAccountByAccountId(accountId);
    }

    @PutMapping(path = "/account/{accountId}")
    public boolean updateAccount(@RequestBody Account account, @PathVariable int accountId) {
        return accountDao.updateAccount(account);
    }
}
