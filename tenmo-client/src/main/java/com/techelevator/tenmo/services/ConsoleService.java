package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private UserService userService = new UserService();

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("6: View your requested pending transfers");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void displayAllUsers(User[] users, AuthenticatedUser currentUser) {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID      Name");
        System.out.println("-------------------------------------------");
        for (User user : users) {
            if (!user.getId().equals(currentUser.getUser().getId())) {
                System.out.println(user.getId() + "    " + user.getUsername());
            }
        }
        System.out.println("---------" + System.lineSeparator());

    }

    public void displayPastTransfers(AuthenticatedUser user, List<Transfer> transfers) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID         From/To         Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            System.out.print(transfer.getTransferId() + "       ");
            String username = "";
            if (transfer.getTransferTypeId() == 1) {
                username = userService.getUsernameByAccountId(user, transfer.getAccountTo());
                System.out.print("From: " + username);
            } else if (transfer.getTransferTypeId() == 2) {
                username = userService.getUsernameByAccountId(user, transfer.getAccountFrom());
                System.out.print("To: " + username);
            }
            System.out.println("        " + transfer.getAmount());

            // need to show transfers from or to currentUser's account. To do this, I need to get username of the account that doesn't match
            // currentUser's accountId and distinguish From or To
        }
        System.out.println("---------");
    }

    public void displayTransferDetails(AuthenticatedUser user, Transfer transfer) {
        String type = "";
        String status = "";
        int typeId = transfer.getTransferTypeId();
        int statusId = transfer.getTransferStatusId();
        String fromUsername = userService.getUsernameByAccountId(user, transfer.getAccountFrom());
        String toUsername = userService.getUsernameByAccountId(user, transfer.getAccountTo());

        if (typeId == 1) {
            type = "REQUEST";
        } else if (typeId == 2) {
            type = "SEND";
        }
        if (statusId == 2) {
            status = "APPROVED";
        } else if (statusId == 3) {
            status = "REJECTED";
        }

        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + fromUsername);
        System.out.println("To: " + toUsername);
        System.out.println("Type: " + type);
        System.out.println("Status: " + status);
        System.out.println("Amount: $" + transfer.getAmount());
    }

    public void displayPendingTransfers(AuthenticatedUser user, List<Transfer> transfers) {
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID        To                Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            String username = userService.getUsernameByAccountId(user, transfer.getAccountTo());
            System.out.println(transfer.getTransferId() + "          " + username + "         " + transfer.getAmount());
        }
        System.out.println("---------");
        // create object server side to send all pending transfer info?

    }

    public void approveOrRejectPendingTransfer() {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
