package com.uny.unydatabaseredmine.auth.payload.request;


import lombok.Getter;
import lombok.Setter;

public class SignupRequest  {

  private String username;


  private String email;


  private String password;

  @Getter
  @Setter
  private String tgName;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String job_title;

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
