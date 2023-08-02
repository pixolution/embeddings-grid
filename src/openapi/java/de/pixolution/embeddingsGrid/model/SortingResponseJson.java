package de.pixolution.embeddingsGrid.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.pixolution.embeddingsGrid.model.SortingResponseGridJson;
import de.pixolution.embeddingsGrid.model.SortingResponseSortedImagesInnerJson;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * SortingResponseJson
 */

@JsonTypeName("SortingResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SortingResponseJson {

  @Valid
  private List<@Valid SortingResponseSortedImagesInnerJson> sortedImages = new ArrayList<>();

  private SortingResponseGridJson grid;

  public SortingResponseJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SortingResponseJson(List<@Valid SortingResponseSortedImagesInnerJson> sortedImages, SortingResponseGridJson grid) {
    this.sortedImages = sortedImages;
    this.grid = grid;
  }

  public SortingResponseJson sortedImages(List<@Valid SortingResponseSortedImagesInnerJson> sortedImages) {
    this.sortedImages = sortedImages;
    return this;
  }

  public SortingResponseJson addSortedImagesItem(SortingResponseSortedImagesInnerJson sortedImagesItem) {
    if (this.sortedImages == null) {
      this.sortedImages = new ArrayList<>();
    }
    this.sortedImages.add(sortedImagesItem);
    return this;
  }

  /**
   * The image uid and its position on the grid
   * @return sortedImages
  */
  @NotNull @Valid 
  @Schema(name = "sorted_images", description = "The image uid and its position on the grid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sorted_images")
  public List<@Valid SortingResponseSortedImagesInnerJson> getSortedImages() {
    return sortedImages;
  }

  public void setSortedImages(List<@Valid SortingResponseSortedImagesInnerJson> sortedImages) {
    this.sortedImages = sortedImages;
  }

  public SortingResponseJson grid(SortingResponseGridJson grid) {
    this.grid = grid;
    return this;
  }

  /**
   * Get grid
   * @return grid
  */
  @NotNull @Valid 
  @Schema(name = "grid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("grid")
  public SortingResponseGridJson getGrid() {
    return grid;
  }

  public void setGrid(SortingResponseGridJson grid) {
    this.grid = grid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SortingResponseJson sortingResponse = (SortingResponseJson) o;
    return Objects.equals(this.sortedImages, sortingResponse.sortedImages) &&
        Objects.equals(this.grid, sortingResponse.grid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sortedImages, grid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortingResponseJson {\n");
    sb.append("    sortedImages: ").append(toIndentedString(sortedImages)).append("\n");
    sb.append("    grid: ").append(toIndentedString(grid)).append("\n");
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

