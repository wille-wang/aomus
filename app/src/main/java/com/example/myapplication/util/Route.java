package com.example.myapplication.util;

import java.util.List;

public class Route {
  private String name;
  private List<String> buildings;
  private String length;
  private String time;

  // No-argument constructor required for Firebase
  public Route() {}

  // Constructor to initialize the route object
  public Route(String name, List<String> buildings, String length, String time) {
    this.name = name;
    this.buildings = buildings;
    this.length = length;
    this.time = time;
  }

  // Getter and setter methods for each field
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getBuildings() {
    return buildings;
  }

  public void setBuildings(List<String> buildings) {
    this.buildings = buildings;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
