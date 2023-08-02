package de.pixolution.embeddingsGrid.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Client error, mostly syntax or attribute errors that the client can solve
 */

@Schema(name = "ClientErrorResponse", description = "Client error, mostly syntax or attribute errors that the client can solve")
@JsonTypeName("ClientErrorResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ClientErrorResponseJson {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date timestamp;

  private Integer status;

  private String error;

  private String path;

  public ClientErrorResponseJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ClientErrorResponseJson(Date timestamp, Integer status, String error, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.path = path;
  }

  public ClientErrorResponseJson timestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  */
  @NotNull @Valid 
  @Schema(name = "timestamp", example = "2023-07-29T19:56:10.347Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public ClientErrorResponseJson status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @NotNull 
  @Schema(name = "status", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ClientErrorResponseJson error(String error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
  */
  @NotNull 
  @Schema(name = "error", example = "Wrong parameter value X given, expected Y", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("error")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public ClientErrorResponseJson path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
  */
  @NotNull 
  @Schema(name = "path", example = "/sort", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientErrorResponseJson clientErrorResponse = (ClientErrorResponseJson) o;
    return Objects.equals(this.timestamp, clientErrorResponse.timestamp) &&
        Objects.equals(this.status, clientErrorResponse.status) &&
        Objects.equals(this.error, clientErrorResponse.error) &&
        Objects.equals(this.path, clientErrorResponse.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, error, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientErrorResponseJson {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
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

