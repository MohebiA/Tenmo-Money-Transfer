package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.an.E;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class App {
    private NumberFormat currency = NumberFormat.getCurrencyInstance();
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private final UserServices userServices = new UserServices();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        String token = accountService.setAuthToken(currentUser.getToken());
        String cUser =accountService.setCurrentUser(currentUser.getUser().getUsername()) ;
        if(currentUser != null){
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
            transferService.setCurrentUser(currentUser.getUser().getUsername());
            userServices.setAuthToken(currentUser.getToken());
            userServices.setCurrentUser(currentUser.getUser().getUsername());
        }

        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        BigDecimal balance = accountService.getBalance();
        System.out.println("This is your current balance: " + currency.format(balance));
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        TransferView[] transfers = transferService.transfersList();
        if (transfers != null) {
            System.out.println("--------------------------------------------");
            System.out.println("Transfers");
            System.out.println("ID          From/To                 Amount");
            System.out.println("--------------------------------------------");
            for (TransferView listOfTransfers: transfers) {
                System.out.println(listOfTransfers.transferToString());
            }
            System.out.println("--------------------------------------------");
          int transferId = consoleService.promptForInt("\nPlease enter transfer ID to view details (0 to cancel): ");
            TransferView transfer = null;

            try {
                transfer = transferService.transferDetail(transferId);
                System.out.println("--------------------------------------------");
                System.out.println("Transfer Details");
                System.out.println("--------------------------------------------");

                System.out.println(transfer.detailsToString());
                System.out.println("--------------------------------------------");
            } catch(NullPointerException e){
                System.out.println("Please enter a valid transfer ID");
                BasicLogger.log(e.getMessage());
            }

        }else {
                consoleService.printErrorMessage();
            }

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub and wrote the prompts for transfer

        User[] userList = userServices.userlist();
        TransferView newTransfer = null;
        if(userList != null) {
            if (userList != null) {
                System.out.println("--------------------------------------------");
                System.out.println("Users");
                System.out.println("ID          Name");
                System.out.println("--------------------------------------------");
                for (User listOfUsers: userList) {
                    System.out.println(listOfUsers.userListToString());
                }
                System.out.println("--------------------------------------------");
                int userid = consoleService.promptForInt("\nEnter ID of user you are sending to (0 to cancel):");
                BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount:");
                newTransfer = transferService.createTransfer(currentUser.getUser().getUsername(), userid, transferAmount);


                try {
                    TransferView sendTransfer= transferService.sendTransfer(newTransfer);
                    if(sendTransfer == null){
                        throw new NullPointerException("Your transfer failed to send");
                    } else {
                        System.out.println("Your transfer has been successfully sent!");
                    }
                } catch (Exception e){
                    consoleService.printErrorMessage();
                }



            }
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
