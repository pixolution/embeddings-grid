package de.pixolution.embeddingsGrid.api;

import de.pixolution.embeddingsGrid.model.ClientErrorResponseJson;
import de.pixolution.embeddingsGrid.model.ServerErrorResponseJson;
import de.pixolution.embeddingsGrid.model.SortingRequestJson;
import de.pixolution.embeddingsGrid.model.SortingResponseJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

/**
 * A delegate to be called by the {@link SortApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public interface SortApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /sort : Arrange a list of image embeddings onto a 2D grid
     * &lt;p&gt;Arrange a list of image embeddings onto a 2D grid using FLAS algorithm presented in the paper &lt;a href&#x3D;\&quot;https://arxiv.org/abs/2205.04255v2\&quot;&gt; Improved Evaluation and Generation of Grid Layouts using Distance Preservation Quality and Linear Assignment Sorting&lt;/a&gt;. FLAS is an sorting algorithm optimized for speed while preserving high quality.&lt;p&gt;  &lt;p&gt;The service allows to create sorted 2D grid views out of a list of images represented by an &lt;code&gt;float[]&lt;/code&gt; embedding and an id. The embeddings can have any dimension, but it is important that all elements of an request have the same length. Utilizing the grid with significantly large image embeddings may lead to a perceptible decrease in computational speed during the arrangement calculation, as the algorithm allocates additional processing resources to accommodate the increased complexity of larger embedding data.&lt;/p&gt;  &lt;h2&gt;Features&lt;/h2&gt; &lt;h4&gt;Seeded Placement of Elements:&lt;/h4&gt; &lt;p&gt;When arranging a list of image embeddings using the FLAS algorithm, you can mark selected images as \&quot;seeded.\&quot; These seeded elements will serve as anchor points around which the sorting algorithm will optimize the placement of the remaining images. The FLAS algorithm will then work to create a balanced and visually appealing layout while respecting the fixed positions of the seeded elements&lt;/p&gt;  &lt;h4&gt;Oversized 2x2 Grid Elements:&lt;/h4&gt; &lt;p&gt;The oversized elements feature allows you to create visually stunning grid layouts by designating specific seeded elements to occupy multiple slots on the grid, enlarging them to 2x2 size, and intelligently replicating the images within the area, unlocking new aesthetic possibilities in your image arrangements.&lt;/p&gt;  &lt;h4&gt;Adjustable Size Factor for Adding Free Space:&lt;/h4&gt; &lt;p&gt;To enhance the aesthetic quality of the grid layout, the service offers the ability to adjust a size factor that adds free space between elements. This feature allows you to control the spacing between images, helping to avoid clutter and create a more visually appealing grid. By fine-tuning the size factor, you can strike the right balance between compactness and readability in the resulting grid view.&lt;/p&gt;  &lt;h4&gt;Infinit Grid Using Wrapped Mode:&lt;/h4&gt; &lt;p&gt;The wrapped mode calculation of the grid to produce a seamless image grid tile that can be placed next to another without any visible borders or disruptions, creating the illusion of a continuous and infinite area when these tiles are tiled together.&lt;/p&gt; 
     *
     * @param sortingRequestJson  (optional)
     * @return Sorted image grid (status code 200)
     *         or bad request (status code 400)
     *         or unexpected error (status code 200)
     * @see SortApi#sortPost
     */
    default ResponseEntity<SortingResponseJson> sortPost(SortingRequestJson sortingRequestJson) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"grid\" : { \"columns\" : 10, \"rows\" : 3 }, \"sorted_images\" : [ { \"size_cols\" : 2, \"column\" : 10, \"id\" : \"408468fd-ce11-46e2-b5c0-936a980a42ef\", \"row\" : 6, \"size_rows\" : 2 }, { \"size_cols\" : 2, \"column\" : 10, \"id\" : \"408468fd-ce11-46e2-b5c0-936a980a42ef\", \"row\" : 6, \"size_rows\" : 2 } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
