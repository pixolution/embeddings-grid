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
 * The rows and columns of the grid
 */

@Schema(name = "SortingResponse_grid", description = "The rows and columns of the grid")
@JsonTypeName("SortingResponse_grid")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SortingResponseGridJson {

  private Integer columns;

  private Integer rows;

  public SortingResponseGridJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SortingResponseGridJson(Integer columns, Integer rows) {
    this.columns = columns;
    this.rows = rows;
  }

  public SortingResponseGridJson columns(Integer columns) {
    this.columns = columns;
    return this;
  }

  /**
   * The number of columns of the grid
   * @return columns
  */
  @NotNull 
  @Schema(name = "columns", example = "10", description = "The number of columns of the grid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("columns")
  public Integer getColumns() {
    return columns;
  }

  public void setColumns(Integer columns) {
    this.columns = columns;
  }

  public SortingResponseGridJson rows(Integer rows) {
    this.rows = rows;
    return this;
  }

  /**
   * The number of rows of the grid
   * @return rows
  */
  @NotNull 
  @Schema(name = "rows", example = "6", description = "The number of rows of the grid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rows")
  public Integer getRows() {
    return rows;
  }

  public void setRows(Integer rows) {
    this.rows = rows;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SortingResponseGridJson sortingResponseGrid = (SortingResponseGridJson) o;
    return Objects.equals(this.columns, sortingResponseGrid.columns) &&
        Objects.equals(this.rows, sortingResponseGrid.rows);
  }

  @Override
  public int hashCode() {
    return Objects.hash(columns, rows);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortingResponseGridJson {\n");
    sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
    sb.append("    rows: ").append(toIndentedString(rows)).append("\n");
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

