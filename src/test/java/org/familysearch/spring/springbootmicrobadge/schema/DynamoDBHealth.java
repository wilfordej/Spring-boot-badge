package org.familysearch.spring.springbootmicrobadge.schema;

import org.springframework.boot.actuate.health.Status;

public class DynamoDBHealth {
  private Status status;
  private Long itemCount;
  private Long tableSizeInBytes;

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Long getItemCount() {
    return itemCount;
  }

  public void setItemCount(Long itemCount) {
    this.itemCount = itemCount;
  }

  public Long getTableSizeInBytes() {
    return tableSizeInBytes;
  }

  public void setTableSizeInBytes(Long tableSizeInBytes) {
    this.tableSizeInBytes = tableSizeInBytes;
  }
}
