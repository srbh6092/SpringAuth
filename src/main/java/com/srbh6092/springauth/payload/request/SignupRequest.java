package com.srbh6092.springauth.payload.request;

import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.*;
import java.util.Set;

public class SignupRequest {

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email(message = "Email invalid")
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(max = 120)
    private String password;

    public SignupRequest() {
    }

    public SignupRequest(String username, String email, Set<String> role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
