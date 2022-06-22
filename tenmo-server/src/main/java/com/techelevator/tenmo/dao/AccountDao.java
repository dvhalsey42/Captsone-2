package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalanceByUserId(int accountId);

    Account getAccountByUserId(int userId);

    Account getAccountByAccountId(int accountId);

    boolean updateAccount(Account account);


}
