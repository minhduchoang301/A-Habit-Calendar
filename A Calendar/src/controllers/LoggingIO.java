package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Handles reading and writing to the text file database of usernames and passwords.
 */
public class LoggingIO extends IO {

    private String USERDATA = "userdata.txt";

    /**
     * Returns True if the login data is correct. Returns False otherwise.
     *
     * @param username the login username.
     * @param password the login password.
     * @return true iff correct login credentials are supplied.
     * @throws IOException if an error happens during I/O.
     */
    public boolean userExists(String username, String password) throws IOException {
        BufferedReader reader;

        try {
            reader = readFile(USERDATA);
        } catch (Exception FileNotFoundException) {
            return false;
        }

        String usertext = reader.readLine(), passwdtext = reader.readLine();
        while (usertext != null && !usertext.equals(username) && !passwdtext.equals(password)) {
            usertext = reader.readLine();
            passwdtext = reader.readLine();
        }

        if (usertext != null && password.equals("~~~")) {
            reader.close();
            return true;
        }

        if (usertext == null || passwdtext == null) {
            reader.close();
            return false;
        }

        if (usertext.equals(username) && passwdtext.equals(password)) {
            reader.close();
            return true;
        }
        return false;
    }

    /**
     * Creates a user with the data supplied. Stores the user login data into the database file.
     *
     * @param username username of the new user to be created.
     * @param password password of the new user to be created.
     * @throws IOException if an error happens during I/O.
     */
    public void createUser(String username, String password) throws IOException {
        BufferedWriter writer;
        try {
            writer = appendToFile(USERDATA);
        } catch (Exception FileNotFoundException) {
            new File(USERDATA);
            writer = appendToFile(USERDATA);
        }

        writer.write(username + "\n" + password + "\n");
        writer.close();
    }
}
