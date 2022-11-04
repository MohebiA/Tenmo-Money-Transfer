package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferView;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {


    private static final String BASE_URL = "http://localhost:8080/myTransfers/";
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    private String currentUser;

    public String setAuthToken(String authToken) {
        this.authToken = authToken;
        return authToken;
    }

    public String setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        return currentUser;
    }

    public TransferView[] transfersList() {
        TransferView[] list = null;
        try {
            ResponseEntity<TransferView[]> response = restTemplate.exchange(BASE_URL +  currentUser, HttpMethod.GET, authEntity(), TransferView[].class);
            list = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return list;
    }

    public TransferView transferDetail(int id){
        TransferView transferView = null;
        try {
            ResponseEntity<TransferView> response = restTemplate.exchange(BASE_URL + "/filter?id=" + id, HttpMethod.GET, authEntity(), TransferView.class);
            transferView = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferView;
    }

    //TODO started create script
    public TransferView sendTransfer(TransferView transferView){
        boolean success = false;
        HttpEntity<TransferView> entity = authEntityWithBody(transferView);
        TransferView createdTransfer = null;
        try{
            createdTransfer = restTemplate.postForObject(BASE_URL, entity, TransferView.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return createdTransfer;
    }

    public TransferView createTransfer (String username, int toUserId, BigDecimal transferAmount){
        TransferView transfer = new TransferView();
            transfer.setUsername(username);
            transfer.setToUserId(toUserId);
            transfer.setTransferStatusId(2);
            transfer.setTransferStatusDesc("Approved");
            transfer.setTransferTypeId(2);
            transfer.setTransferTypeDesc("Send");
            transfer.setTransferAmount(transferAmount);
        return transfer;
    }


    public void printTransferListString() {
        TransferView[] list = null;
        try {
            ResponseEntity<TransferView[]> response = restTemplate.exchange(BASE_URL + currentUser, HttpMethod.GET, authEntity(), TransferView[].class);
            list = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (list != null) {
            for (TransferView transfer : list) {
                System.out.println(transfer.transferToString());
            }
        }
    }

    private HttpEntity<TransferView> authEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferView> authEntityWithBody(TransferView transferView) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(transferView, headers);
    }
}
