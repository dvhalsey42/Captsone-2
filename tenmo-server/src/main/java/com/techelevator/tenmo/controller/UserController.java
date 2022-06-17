package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping(path = "/user")
    public List<User> findAll() {
        return userDao.findAll();
    }

    @GetMapping(path = "/user/{accountId}")
    public String getUsernameByAccountId(@PathVariable int accountId) {
        return userDao.getUsernameByAccountId(accountId);
    }
}
