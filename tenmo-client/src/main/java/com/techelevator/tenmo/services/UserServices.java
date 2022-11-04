package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

public class UserServices {


    private static final String BASE_URL = "http://localhost:8080/Users";
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

    public User[] userlist() {
        User[] list = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(BASE_URL , HttpMethod.GET, authEntity(), User[].class);
            list = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return list;
    }

    private HttpEntity<Account> authEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
