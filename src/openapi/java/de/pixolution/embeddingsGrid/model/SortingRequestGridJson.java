package de.pixolution.embeddingsGrid.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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

@Schema(name = "SortingRequest_grid", description = "The rows and columns of the grid")
@JsonTypeName("SortingRequest_grid")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SortingRequestGridJson {

  private Boolean wrappedMode;

  private Integer seed;

  private BigDecimal aspectRatio;

  private Integer sizeFactor = 0;

  public SortingRequestGridJson() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SortingRequestGridJson(Boolean wrappedMode, BigDecimal aspectRatio) {
    this.wrappedMode = wrappedMode;
    this.aspectRatio = aspectRatio;
  }

  public SortingRequestGridJson wrappedMode(Boolean wrappedMode) {
    this.wrappedMode = wrappedMode;
    return this;
  }

  /**
   * Should a endless grid be calculated that wraps on the edges?
   * @return wrappedMode
  */
  @NotNull 
  @Schema(name = "wrapped_mode", example = "true", description = "Should a endless grid be calculated that wraps on the edges?", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("wrapped_mode")
  public Boolean getWrappedMode() {
    return wrappedMode;
  }

  public void setWrappedMode(Boolean wrappedMode) {
    this.wrappedMode = wrappedMode;
  }

  public SortingRequestGridJson seed(Integer seed) {
    this.seed = seed;
    return this;
  }

  /**
   * Random seed to use for reproducible calls
   * @return seed
  */
  
  @Schema(name = "seed", example = "42", description = "Random seed to use for reproducible calls", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seed")
  public Integer getSeed() {
    return seed;
  }

  public void setSeed(Integer seed) {
    this.seed = seed;
  }

  public SortingRequestGridJson aspectRatio(BigDecimal aspectRatio) {
    this.aspectRatio = aspectRatio;
    return this;
  }

  /**
   * The ratio between the columns and rows of the grid as float number. Below 1.0 means portrait, 1.0 is squared, above 1.0 means landscape shape. Some examples:   - 1:1 is 1.0   - 4:3 is 1.33   - 16:9 is 1.77 
   * minimum: 0.0
   * @return aspectRatio
  */
  @NotNull @Valid @DecimalMin(value = "0.0", inclusive = false) 
  @Schema(name = "aspect_ratio", example = "1.33", description = "The ratio between the columns and rows of the grid as float number. Below 1.0 means portrait, 1.0 is squared, above 1.0 means landscape shape. Some examples:   - 1:1 is 1.0   - 4:3 is 1.33   - 16:9 is 1.77 ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("aspect_ratio")
  public BigDecimal getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(BigDecimal aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  public SortingRequestGridJson sizeFactor(Integer sizeFactor) {
    this.sizeFactor = sizeFactor;
    return this;
  }

  /**
   * A integer between 1 and 100 that means the percentage of additional space the canvas should provide.
   * minimum: 0
   * maximum: 1000
   * @return sizeFactor
  */
  @Min(0) @Max(1000) 
  @Schema(name = "size_factor", example = "17", description = "A integer between 1 and 100 that means the percentage of additional space the canvas should provide.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size_factor")
  public Integer getSizeFactor() {
    return sizeFactor;
  }

  public void setSizeFactor(Integer sizeFactor) {
    this.sizeFactor = sizeFactor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SortingRequestGridJson sortingRequestGrid = (SortingRequestGridJson) o;
    return Objects.equals(this.wrappedMode, sortingRequestGrid.wrappedMode) &&
        Objects.equals(this.seed, sortingRequestGrid.seed) &&
        Objects.equals(this.aspectRatio, sortingRequestGrid.aspectRatio) &&
        Objects.equals(this.sizeFactor, sortingRequestGrid.sizeFactor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrappedMode, seed, aspectRatio, sizeFactor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortingRequestGridJson {\n");
    sb.append("    wrappedMode: ").append(toIndentedString(wrappedMode)).append("\n");
    sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
    sb.append("    aspectRatio: ").append(toIndentedString(aspectRatio)).append("\n");
    sb.append("    sizeFactor: ").append(toIndentedString(sizeFactor)).append("\n");
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

