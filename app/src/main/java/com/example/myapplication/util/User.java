package com.example.myapplication.util;

import java.util.Map;

// Represent the schema of the User object in the database
public class User {

  private String username;
  private String password;
  private String program;
  private String realName;
  private String schoolEmail;
  private Map<Integer, CheckInInfo> checkins;

  public User() {}

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(
      String username, String password, String program, String realName, String schoolEmail) {
    this.username = username;
    this.password = password;
    this.program = program;
    this.realName = realName;
    this.schoolEmail = schoolEmail;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getProgram() {
    return program;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getSchoolEmail() {
    return schoolEmail;
  }

  public void setSchoolEmail(String schoolEmail) {
    this.schoolEmail = schoolEmail;
  }

  public Map<Integer, CheckInInfo> getCheckins() {
    return checkins;
  }

  public void setCheckins(Map<Integer, CheckInInfo> checkins) {
    this.checkins = checkins;
  }

  public static class CheckInInfo {
    private int counts;
    private long lastCheckIn;

    public CheckInInfo() {}

    public CheckInInfo(int counts, long lastCheckIn) {
      this.counts = counts;
      this.lastCheckIn = lastCheckIn;
    }

    public int getCounts() {
      return counts;
    }

    public void setCounts(int counts) {
      this.counts = counts;
    }

    public long getLastCheckIn() {
      return lastCheckIn;
    }

    public void setLastCheckIn(long lastCheckIn) {
      this.lastCheckIn = lastCheckIn;
    }
  }
}
