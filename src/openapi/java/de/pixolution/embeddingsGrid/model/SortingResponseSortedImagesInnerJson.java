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
 * SortingResponseSortedImagesInnerJson
 */

@JsonTypeName("SortingResponse_sorted_images_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SortingResponseSortedImagesInnerJson {

  private String id;

  private Integer row;

  private Integer column;

  private Integer sizeCols = 1;

  private Integer sizeRows = 1;

  public SortingResponseSortedImagesInnerJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SortingResponseSortedImagesInnerJson(String id, Integer row, Integer column, Integer sizeCols, Integer sizeRows) {
    this.id = id;
    this.row = row;
    this.column = column;
    this.sizeCols = sizeCols;
    this.sizeRows = sizeRows;
  }

  public SortingResponseSortedImagesInnerJson id(String id) {
    this.id = id;
    return this;
  }

  /**
   * page Id of the flyer page
   * @return id
  */
  @NotNull 
  @Schema(name = "id", example = "408468fd-ce11-46e2-b5c0-936a980a42ef", description = "page Id of the flyer page", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SortingResponseSortedImagesInnerJson row(Integer row) {
    this.row = row;
    return this;
  }

  /**
   * The row the image is placed on the grid.
   * minimum: 0
   * @return row
  */
  @NotNull @Min(0) 
  @Schema(name = "row", example = "6", description = "The row the image is placed on the grid.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("row")
  public Integer getRow() {
    return row;
  }

  public void setRow(Integer row) {
    this.row = row;
  }

  public SortingResponseSortedImagesInnerJson column(Integer column) {
    this.column = column;
    return this;
  }

  /**
   * The column the image is placed on the grid.
   * minimum: 0
   * @return column
  */
  @NotNull @Min(0) 
  @Schema(name = "column", example = "10", description = "The column the image is placed on the grid.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("column")
  public Integer getColumn() {
    return column;
  }

  public void setColumn(Integer column) {
    this.column = column;
  }

  public SortingResponseSortedImagesInnerJson sizeCols(Integer sizeCols) {
    this.sizeCols = sizeCols;
    return this;
  }

  /**
   * Number of columns the image span over
   * minimum: 1
   * @return sizeCols
  */
  @NotNull @Min(1) 
  @Schema(name = "size_cols", example = "2", description = "Number of columns the image span over", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size_cols")
  public Integer getSizeCols() {
    return sizeCols;
  }

  public void setSizeCols(Integer sizeCols) {
    this.sizeCols = sizeCols;
  }

  public SortingResponseSortedImagesInnerJson sizeRows(Integer sizeRows) {
    this.sizeRows = sizeRows;
    return this;
  }

  /**
   * Number of rows the image span over
   * minimum: 1
   * @return sizeRows
  */
  @NotNull @Min(1) 
  @Schema(name = "size_rows", example = "2", description = "Number of rows the image span over", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size_rows")
  public Integer getSizeRows() {
    return sizeRows;
  }

  public void setSizeRows(Integer sizeRows) {
    this.sizeRows = sizeRows;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SortingResponseSortedImagesInnerJson sortingResponseSortedImagesInner = (SortingResponseSortedImagesInnerJson) o;
    return Objects.equals(this.id, sortingResponseSortedImagesInner.id) &&
        Objects.equals(this.row, sortingResponseSortedImagesInner.row) &&
        Objects.equals(this.column, sortingResponseSortedImagesInner.column) &&
        Objects.equals(this.sizeCols, sortingResponseSortedImagesInner.sizeCols) &&
        Objects.equals(this.sizeRows, sortingResponseSortedImagesInner.sizeRows);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, row, column, sizeCols, sizeRows);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortingResponseSortedImagesInnerJson {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    row: ").append(toIndentedString(row)).append("\n");
    sb.append("    column: ").append(toIndentedString(column)).append("\n");
    sb.append("    sizeCols: ").append(toIndentedString(sizeCols)).append("\n");
    sb.append("    sizeRows: ").append(toIndentedString(sizeRows)).append("\n");
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

