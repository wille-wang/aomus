package com.example.myapplication.util;

public class Building {

  private String campus; // campus where the building is located
  private String code; // unique building code
  private String desc; // description of the building
  private String googleMapPlusCode; // Google Maps Plus code for location
  private String imgUrl; // URL of the building's image
  private String name; // name of the building
  private int year; // year the building was established

  // Constructor to initialize the building object
  public Building(
      String campus,
      String code,
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
