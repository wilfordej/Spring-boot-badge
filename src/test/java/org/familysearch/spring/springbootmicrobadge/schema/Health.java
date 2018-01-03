package org.familysearch.spring.springbootmicrobadge.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Health {
  private String status;
  private DiskSpace diskSpace;
  private DynamoDBHealth dynamo;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public DiskSpace getDiskSpace() {
    return diskSpace;
  }

  public void setDiskSpace(DiskSpace diskSpace) {
    this.diskSpace = diskSpace;
  }

  public DynamoDBHealth getDynamo() {
    return dynamo;
  }

  public void setDynamo(DynamoDBHealth dynamo) {
    this.dynamo = dynamo;
  }
}
