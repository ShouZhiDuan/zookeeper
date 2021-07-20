package com.example.zk.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


public class StudyInfo implements Serializable {

  public StudyInfo() {

  }

  public StudyInfo(StudyInfo other) {
    this.userId = other.userId;
    this.name = other.name;
    this.description = other.description;
    this.startDate = other.startDate;
    this.endDate = other.endDate;
    this.privacy = other.privacy;
  }

  @Setter
  private Long userId;

  @Setter
  private String name;

  @Setter
  private String startDate;

  @Setter
  private String endDate;

  @Setter
  private String description;

  @Getter
  @Setter
  private StudyPrivacy privacy;

  @Override public String toString(){
    return "{\"userId\":" + userId
        + ", \"name\": \"" + name
        + "\", \"startDate\": \"" + startDate
        + "\", \"endDate\": \"" + endDate
        + "\", \"description\": \"" + description
//        + "\", \"privacy\": \"" + privacy.toString()
        + "\"}";
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public StudyPrivacy getPrivacy() {
    return privacy;
  }

  public void setPrivacy(StudyPrivacy privacy) {
    this.privacy = privacy;
  }
}
