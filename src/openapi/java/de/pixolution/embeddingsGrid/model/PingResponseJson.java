package de.pixolution.embeddingsGrid.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Simple JSON ping response endpoint
 */

@Schema(name = "PingResponse", description = "Simple JSON ping response endpoint")
@JsonTypeName("PingResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class PingResponseJson {

  private String msg;

  public PingResponseJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PingResponseJson(String msg) {
    this.msg = msg;
  }

  public PingResponseJson msg(String msg) {
    this.msg = msg;
    return this;
  }

  /**
   * Get msg
   * @return msg
  */
  @NotNull 
  @Schema(name = "msg", example = "Ping back", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("msg")
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PingResponseJson pingResponse = (PingResponseJson) o;
    return Objects.equals(this.msg, pingResponse.msg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(msg);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PingResponseJson {\n");
    sb.append("    msg: ").append(toIndentedString(msg)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

