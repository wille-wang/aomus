package com.example.myapplication.util;

// Represent the schema of the Building object in the `buildings` node in the Firebase Realtime
// Database
public class Building {

  private String code;
  private String desc;
  private String googleMapPlusCode;
  private String imgUrl;
  private String name;
  private int year;

  // No-argument constructor required for Firebase
  public Building() {}

  // Constructor to initialize the building object
  public Building(
      String code, String desc, String googleMapPlusCode, String imgUrl, String name, int year) {
    this.code = code;
    this.desc = desc;
    this.googleMapPlusCode = googleMapPlusCode;
    this.imgUrl = imgUrl;
    this.name = name;
    this.year = year;
  }

  // Getter and setter methods for each field

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getGoogleMapPlusCode() {
    return googleMapPlusCode;
  }

  public void setGoogleMapPlusCode(String googleMapPlusCode) {
    this.googleMapPlusCode = googleMapPlusCode;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }
}
