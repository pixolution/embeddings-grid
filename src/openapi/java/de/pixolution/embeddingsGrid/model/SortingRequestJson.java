package de.pixolution.embeddingsGrid.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.pixolution.embeddingsGrid.model.SortingRequestGridJson;
import de.pixolution.embeddingsGrid.model.SortingRequestImagesInnerJson;
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
 * Request to arrange a given list of images with respect to the given grid specification. 
 */

@Schema(name = "SortingRequest", description = "Request to arrange a given list of images with respect to the given grid specification. ")
@JsonTypeName("SortingRequest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SortingRequestJson {

  @Valid
  private List<@Valid SortingRequestImagesInnerJson> images = new ArrayList<>();

  private SortingRequestGridJson grid;

  public SortingRequestJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SortingRequestJson(List<@Valid SortingRequestImagesInnerJson> images, SortingRequestGridJson grid) {
    this.images = images;
    this.grid = grid;
  }

  public SortingRequestJson images(List<@Valid SortingRequestImagesInnerJson> images) {
    this.images = images;
    return this;
  }

  public SortingRequestJson addImagesItem(SortingRequestImagesInnerJson imagesItem) {
    if (this.images == null) {
      this.images = new ArrayList<>();
    }
    this.images.add(imagesItem);
    return this;
  }

  /**
   * Get images
   * @return images
  */
  @NotNull @Valid 
  @Schema(name = "images", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("images")
  public List<@Valid SortingRequestImagesInnerJson> getImages() {
    return images;
  }

  public void setImages(List<@Valid SortingRequestImagesInnerJson> images) {
    this.images = images;
  }

  public SortingRequestJson grid(SortingRequestGridJson grid) {
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
  public SortingRequestGridJson getGrid() {
    return grid;
  }

  public void setGrid(SortingRequestGridJson grid) {
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
    SortingRequestJson sortingRequest = (SortingRequestJson) o;
    return Objects.equals(this.images, sortingRequest.images) &&
        Objects.equals(this.grid, sortingRequest.grid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(images, grid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortingRequestJson {\n");
    sb.append("    images: ").append(toIndentedString(images)).append("\n");
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

