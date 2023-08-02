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
 * SortingRequestImagesInnerJson
 */

@JsonTypeName("SortingRequest_images_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SortingRequestImagesInnerJson {

  private String id;

  private String embedding;

  private Integer seedColumn = -1;

  private Integer seedRow = -1;

  private Integer sizeCols = 1;

  private Integer sizeRows = 1;

  public SortingRequestImagesInnerJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SortingRequestImagesInnerJson(String id, String embedding) {
    this.id = id;
    this.embedding = embedding;
  }

  public SortingRequestImagesInnerJson id(String id) {
    this.id = id;
    return this;
  }

  /**
   * page Id of the flyer page
   * @return id
  */
  @NotNull @Size(min = 1, max = 512) 
  @Schema(name = "id", example = "408468fd-ce11", description = "page Id of the flyer page", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SortingRequestImagesInnerJson embedding(String embedding) {
    this.embedding = embedding;
    return this;
  }

  /**
   * Base64 encoded list of floats
   * @return embedding
  */
  @NotNull @Size(min = 1) 
  @Schema(name = "embedding", example = "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==", description = "Base64 encoded list of floats", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("embedding")
  public String getEmbedding() {
    return embedding;
  }

  public void setEmbedding(String embedding) {
    this.embedding = embedding;
  }

  public SortingRequestImagesInnerJson seedColumn(Integer seedColumn) {
    this.seedColumn = seedColumn;
    return this;
  }

  /**
   * The column coordinate this image should be seeded on the grid
   * @return seedColumn
  */
  
  @Schema(name = "seed_column", example = "2", description = "The column coordinate this image should be seeded on the grid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seed_column")
  public Integer getSeedColumn() {
    return seedColumn;
  }

  public void setSeedColumn(Integer seedColumn) {
    this.seedColumn = seedColumn;
  }

  public SortingRequestImagesInnerJson seedRow(Integer seedRow) {
    this.seedRow = seedRow;
    return this;
  }

  /**
   * The row coordinate this image should be seeded on grid
   * @return seedRow
  */
  
  @Schema(name = "seed_row", example = "3", description = "The row coordinate this image should be seeded on grid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seed_row")
  public Integer getSeedRow() {
    return seedRow;
  }

  public void setSeedRow(Integer seedRow) {
    this.seedRow = seedRow;
  }

  public SortingRequestImagesInnerJson sizeCols(Integer sizeCols) {
    this.sizeCols = sizeCols;
    return this;
  }

  /**
   * The number of columns the element should span over
   * minimum: 1
   * maximum: 2
   * @return sizeCols
  */
  @Min(1) @Max(2) 
  @Schema(name = "size_cols", example = "2", description = "The number of columns the element should span over", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size_cols")
  public Integer getSizeCols() {
    return sizeCols;
  }

  public void setSizeCols(Integer sizeCols) {
    this.sizeCols = sizeCols;
  }

  public SortingRequestImagesInnerJson sizeRows(Integer sizeRows) {
    this.sizeRows = sizeRows;
    return this;
  }

  /**
   * The number of columns the element should span over
   * minimum: 1
   * maximum: 2
   * @return sizeRows
  */
  @Min(1) @Max(2) 
  @Schema(name = "size_rows", example = "2", description = "The number of columns the element should span over", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    SortingRequestImagesInnerJson sortingRequestImagesInner = (SortingRequestImagesInnerJson) o;
    return Objects.equals(this.id, sortingRequestImagesInner.id) &&
        Objects.equals(this.embedding, sortingRequestImagesInner.embedding) &&
        Objects.equals(this.seedColumn, sortingRequestImagesInner.seedColumn) &&
        Objects.equals(this.seedRow, sortingRequestImagesInner.seedRow) &&
        Objects.equals(this.sizeCols, sortingRequestImagesInner.sizeCols) &&
        Objects.equals(this.sizeRows, sortingRequestImagesInner.sizeRows);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, embedding, seedColumn, seedRow, sizeCols, sizeRows);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortingRequestImagesInnerJson {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    embedding: ").append(toIndentedString(embedding)).append("\n");
    sb.append("    seedColumn: ").append(toIndentedString(seedColumn)).append("\n");
    sb.append("    seedRow: ").append(toIndentedString(seedRow)).append("\n");
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

