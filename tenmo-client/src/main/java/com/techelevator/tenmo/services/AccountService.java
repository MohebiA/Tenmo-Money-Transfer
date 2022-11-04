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

    private static final String BASE_URL = "http://localhost:8080/myAccount/";
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    private String currentUser;

    public String setAuthToken(String authToken){
        this.authToken = authToken;
        return authToken;
    }
    public String setCurrentUser(String currentUser){
        this.currentUser = currentUser;
        return currentUser;
    }

    public BigDecimal getBalance(){
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();

        BigDecimal balance = new BigDecimal(0);
        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_URL + currentUser, HttpMethod.GET, authEntity(), BigDecimal.class);
            balance = response.getBody();

        } catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return balance;
    }

    private HttpEntity<Account> authEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
        }
}
