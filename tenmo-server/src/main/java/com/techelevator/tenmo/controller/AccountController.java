package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
       this.accountDao = accountDao;
       this.userDao = userDao;
    }

    @GetMapping(path = "balance/{userId}")
    public BigDecimal getBalanceById(@PathVariable int userId) {
        return accountDao.getBalance(userId);
    }

    @GetMapping(path = "/user")
    public List<User> findAll() {
        return userDao.findAll();
    }
}
