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
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080";
    RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getBalance(AuthenticatedUser user, Long userId) {
        BigDecimal balance = null;

        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(API_BASE_URL + "/balance/" + userId, HttpMethod.GET, makeAuthEntity(user), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public User[] getAllUsers(AuthenticatedUser user) {
//        List<User> users = new ArrayList<>();
        User[] users = null;

        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "/user", HttpMethod.GET, makeAuthEntity(user), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
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
