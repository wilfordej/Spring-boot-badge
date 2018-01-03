package org.familysearch.spring.springbootmicrobadge.service;

public class PersonSummary {
  private String firstName;
  private String lastName;

  public PersonSummary() {
  }

  public PersonSummary(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
