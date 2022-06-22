package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080";
    RestTemplate restTemplate = new RestTemplate();

    public AccountService() {

    }
    //Takes an authenticaed user and sends get request to server to receive balance of
    //current user
    public BigDecimal getBalance(AuthenticatedUser user) {
        BigDecimal balance = null;

        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(API_BASE_URL + "/balance/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }
    //Requests an account object from the server that matches the input userId
    public Account getAccountByUserId(AuthenticatedUser user, Long userId) {
        Account account = null;

        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "/account?user_id=" + userId, HttpMethod.GET, makeAuthEntity(user), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }
    //Takes accountId int and requests corresponding full account object from server
    public Account getAccountByAccountId(AuthenticatedUser user, int accountId) {
        Account account = null;

        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "/account?account_id=" + accountId, HttpMethod.GET, makeAuthEntity(user), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }
    //takes a full account object input and updates the info in the server
    public boolean updateAccount(Account account, AuthenticatedUser user) {
        boolean result = false;
        try {
            ResponseEntity<Boolean> response =
                    restTemplate.exchange(API_BASE_URL + "/account", HttpMethod.PUT, makeAccountEntity(account, user), boolean.class);
            result = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    private HttpEntity<Account> makeAccountEntity(Account account, AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }
}
