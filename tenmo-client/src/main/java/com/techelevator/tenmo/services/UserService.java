package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {

    private static final String API_BASE_URL = "http://localhost:8080";
    RestTemplate restTemplate = new RestTemplate();

    //returns a list of all users
    public User[] getAllUsers(AuthenticatedUser user) {
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
    //returns a user object by corresponding account id
    public String getUsernameByAccountId(AuthenticatedUser user, int accountId) {
        String username = "";
        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(API_BASE_URL + "/user/" + accountId, HttpMethod.GET,makeAuthEntity(user), String.class);
                username = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }
}
