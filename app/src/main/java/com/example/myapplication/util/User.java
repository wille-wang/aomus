package com.example.myapplication.util;

import java.util.HashMap;
import java.util.Map;

// Represent the schema of the User object in the `users` node in the Firebase Realtime Database
public class User {

  private String username;
  private String password;
  private String program;
  private String realName;
  private String schoolEmail;
  private Map<String, CheckInInfo> checkins;

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

  public Map<String, CheckInInfo> getCheckins() {
    return checkins;
  }

  public void setCheckins(Map<String, CheckInInfo> checkins) {
    this.checkins = checkins;
  }

  public void updateVisitCount(String buildingId) {
    if (checkins == null) {
      checkins = new HashMap<>();
    }
    CheckInInfo checkInInfo =
        checkins.getOrDefault(
            buildingId, new CheckInInfo(buildingId, 0, System.currentTimeMillis()));
    checkInInfo.setCounts(checkInInfo.getCounts() + 1);
    checkInInfo.setLastCheckIn(System.currentTimeMillis());
    checkins.put(buildingId, checkInInfo);
  }

  public static class CheckInInfo {
    private String buildingCode;
    private int counts;
    private long lastCheckIn;

    public CheckInInfo() {}

    public CheckInInfo(String buildingCode, int counts, long lastCheckIn) {
      this.buildingCode = buildingCode;
      this.counts = counts;
      this.lastCheckIn = lastCheckIn;
    }

    public String getBuildingCode() {
      return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
      this.buildingCode = buildingCode;
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
