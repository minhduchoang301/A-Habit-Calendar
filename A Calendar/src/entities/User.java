package entities;

import java.time.LocalDateTime;

public class User {
    private String username;
    private String password;
    /* store and verify log-in information. username must be unique */
    public User(String Username, String Password){
        this.username = Username;
        this.password = Password;
    }

    public boolean LogIn(String Username, String Password){
        return (username.equals(Username)) && (password.equals(Password));
    }


}
