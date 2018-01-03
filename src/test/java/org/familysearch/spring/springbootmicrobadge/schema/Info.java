package org.familysearch.spring.springbootmicrobadge.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {
  private InfoDetails infoDetails;

  @JsonProperty(value = "app")
  public InfoDetails getInfoDetails() {
    return infoDetails;
  }

  public void setInfoDetails(InfoDetails infoDetails) {
    this.infoDetails = infoDetails;
  }


  public class InfoDetails {
    String name;
    String description;
    String version;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }
  }
}
