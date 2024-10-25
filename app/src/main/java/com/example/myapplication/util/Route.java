package com.example.myapplication.util;

import java.util.List;

public class Route {
  private String name;
  private List<String> buildings;
  private String length;
  private String time;

  // 空构造函数（Firebase 需要）
  public Route() {}

  // 参数化构造函数
  public Route(String name, List<String> buildings, String length, String time) {
    this.name = name;
    this.buildings = buildings;
    this.length = length;
    this.time = time;
  }

  // Getter 和 Setter 方法
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
