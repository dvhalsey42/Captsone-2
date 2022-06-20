package com.techelevator.tenmo;

import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

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
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    //add pending transfers notification on login
    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            if(transferService.getPendingTransfers(currentUser).size() > 0){
                System.out.println("\n***You have pending transfers***");
            }
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
            } else if (menuSelection == 6) {
                viewUserPendingTransfers();
            } else if (menuSelection == 0) {
                System.exit(1);
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewUserPendingTransfers(){
        List<Transfer> transfers = transferService.getUsersPendingTransfers(currentUser);
        consoleService.displayPendingTransfers(currentUser, transfers);
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is: " + accountService.getBalance(currentUser));
        consoleService.pause();
        mainMenu();
	}

	private void viewTransferHistory() {
        List<Transfer> transfers = transferService.getPastTransfers(currentUser);
        consoleService.displayPastTransfers(currentUser, transfers);
        int transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if (transferId == 0) {
            mainMenu();
        }
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == transferId) {
                consoleService.displayTransferDetails(currentUser, transfer);

            }
        }
        consoleService.pause();



    }

	private void viewPendingRequests() {
        List<Transfer> transfers = transferService.getPendingTransfers(currentUser);
        consoleService.displayPendingTransfers(currentUser, transfers);
        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        if (transferId == 0) {
            mainMenu();
        }
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == transferId) {
                consoleService.approveOrRejectPendingTransfer();
                int choice = consoleService.promptForInt("Please choose an option: ");
                if (choice == 1 && (transfer.getAmount().compareTo(accountService.getBalance(currentUser)) <= 0)) {
                    transferService.approveTransfer(currentUser, transfer);
                //update account balances
                    Account fromAccount = accountService.getAccountByAccountId(currentUser, transfer.getAccountFrom());
                    Account toAccount = accountService.getAccountByAccountId(currentUser, transfer.getAccountTo());
                    fromAccount.setBalance(fromAccount.getBalance().subtract(transfer.getAmount()));
                    toAccount.setBalance(toAccount.getBalance().add(transfer.getAmount()));
                    boolean fromSuccess = accountService.updateAccount(fromAccount, currentUser);
                    boolean toSuccess = accountService.updateAccount(toAccount, currentUser);
                    if (fromSuccess && toSuccess) {
                        System.out.println("Success!");
                    } else {
                    System.out.println("Failure");
                    }
                }else if(choice == 1){
                    System.out.println("Insufficient Funds");
                }
                else if (choice == 2) {
                    transferService.rejectTransfer(currentUser, transfer);
                } else {
                    mainMenu();
                }

            }
        }


		
	}

	private void sendBucks() {

        consoleService.displayAllUsers(userService.getAllUsers(currentUser), currentUser);
        int userId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");
        if (userId == 0) {
            mainMenu();
        }

        boolean isRealUser = false;
        for(User user:userService.getAllUsers(currentUser)){
            if(userId == user.getId()){
                isRealUser = true;
            }
        }

        if(isRealUser) {
            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount:");
            if (amount.signum() > 0 && amount.compareTo(accountService.getBalance(currentUser)) <= 0) {

                int fromAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
                int toAccountId = accountService.getAccountByUserId(currentUser, (long) userId).getAccountId();

                if (transferService.createTransfer(currentUser, 2, 2, fromAccountId, toAccountId, amount)) {
                    //call transfer logic methods
                    Account fromAccount = accountService.getAccountByAccountId(currentUser, fromAccountId);
                    Account toAccount = accountService.getAccountByAccountId(currentUser, toAccountId);
                    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                    toAccount.setBalance(toAccount.getBalance().add(amount));
                    boolean fromSuccess = accountService.updateAccount(fromAccount, currentUser);
                    boolean toSuccess = accountService.updateAccount(toAccount, currentUser);

                    //write out success message
                    if (fromSuccess && toSuccess) {
                        System.out.println("Success!");
                    }
                } else {
                    System.out.println("Failure");
                }
            } else {
                System.out.println("Insufficient Funds");
            }
        }
        else{
            System.out.println("Invalid user selected");
        }
	}

	private void requestBucks() {

        consoleService.displayAllUsers(userService.getAllUsers(currentUser), currentUser);
        int userId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel):");
        if (userId == 0) {
            mainMenu();
        }
        boolean isRealUser = false;
        for(User user:userService.getAllUsers(currentUser)){
            if(userId == user.getId()){
                isRealUser = true;
            }
        }
        if(isRealUser) {
            BigDecimal amount = consoleService.promptForBigDecimal("Enter amount:");
            if (amount.signum() > 0 && userId != currentUser.getUser().getId()) {
                int fromAccountId = accountService.getAccountByUserId(currentUser, (long) userId).getAccountId();
                int toAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();

                transferService.createTransfer(currentUser, 1, 1, fromAccountId, toAccountId, amount);

            }
        }
        else{
            System.out.println("Invalid user selected");
        }
    }

}
