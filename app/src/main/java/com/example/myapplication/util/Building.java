package com.example.myapplication.util;

public class Building {

  private String campus;
  private int code;
  private String desc;
  private String googleMapPlusCode;
  private String imgUrl;
  private String name;
  private int year;

  // No-argument constructor required for Firebase
  public Building() {}

  // Constructor to initialize the building object
  public Building(
      String campus,
      int code,
      String desc,
      String googleMapPlusCode,
      String imgUrl,
      String name,
      int year) {
    this.campus = campus;
    this.code = code;
    this.desc = desc;
    this.googleMapPlusCode = googleMapPlusCode;
    this.imgUrl = imgUrl;
    this.name = name;
    this.year = year;
  }

  // Getter and setter methods for each field
  public String getCampus() {
    return campus;
  }

  public void setCampus(String campus) {
    this.campus = campus;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
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
