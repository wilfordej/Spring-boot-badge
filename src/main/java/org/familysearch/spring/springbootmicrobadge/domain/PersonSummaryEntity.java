package org.familysearch.spring.springbootmicrobadge.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "")
public class PersonSummaryEntity {

  private String personId;
  private String firstName;
  private String lastName;

  public PersonSummaryEntity() {
  }

  public PersonSummaryEntity(String personId, String firstName, String lastName) {
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @DynamoDBHashKey(attributeName = "personId")
  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  @DynamoDBAttribute
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @DynamoDBAttribute
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
