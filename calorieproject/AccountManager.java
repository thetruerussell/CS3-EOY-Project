package com.example.calorieproject;

import java.io.*;
import java.util.Scanner;

public class AccountManager {
    private static final String FILE_PATH = "Accounts.txt";

    public static void saveAccount(Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String data = String.format("%s,%s,%d,%s\n",
                    account.getUsername(),
                    account.getPassword(),
                    account.getAccountNumber(),
                    account.getAccountHolderName());
            writer.write(data);
        } catch (IOException e) {
            System.err.println("Error saving account: " + e.getMessage());
        }
    }

    public static boolean login(String username, String password) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
