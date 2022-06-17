package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080";
    RestTemplate restTemplate = new RestTemplate();

    public boolean createTransfer(AuthenticatedUser user, int transferTypeId, int transferStatusId, int fromAccountId, int toAccountId, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(transferTypeId);
        transfer.setTransferStatusId(transferStatusId);
        transfer.setAccountFrom(fromAccountId);
        transfer.setAccountTo(toAccountId);
        transfer.setAmount(amount);

        ResponseEntity<Boolean> response =
                restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.POST,
                        makeTransferEntity(transfer, user), boolean.class);
        if(response.getBody() != null) {
            return response.getBody();
        }
        return false;
    }

    public List<Transfer> getPastTransfers(AuthenticatedUser user) {
        List<Transfer> pastTransfers = new ArrayList<>();
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfers/" +
                    user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfer[].class);
            pastTransfers = Arrays.asList((Transfer[])Objects.requireNonNull((Transfer[])response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pastTransfers;
    }

    public List<Transfer> getPendingTransfers(AuthenticatedUser user) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfers/pending/" +
                    user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfer[].class);
            transfers = Arrays.asList((Transfer[]) Objects.requireNonNull((Transfer[]) response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    // make sure transferId is valid
    // display to approve or reject
    // update accounts and change transfer status
    public boolean approveTransfer(AuthenticatedUser user, Transfer transfer) {
        boolean success = false;

        transfer.setTransferStatusId(2);
        try {
            ResponseEntity<Boolean> response =
                    restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.PUT, makeTransferEntity(transfer, user), boolean.class);
            if(response.getBody() != null) {
                success = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean rejectTransfer(AuthenticatedUser user, Transfer transfer) {
        boolean success = false;

        transfer.setTransferStatusId(3);
        try {
            ResponseEntity<Boolean> response =
                    restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.PUT, makeTransferEntity(transfer, user), boolean.class);
            if(response.getBody() != null) {
                success = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }
}
