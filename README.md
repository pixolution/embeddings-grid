# OpenAPI generated server

Spring Boot Server that implements a self-organizing-map to turn a list of images
with embeddings to a 2D image grid. It implements LAS and FLAS described in the paper [Improved Evaluation and Generation of Grid Layouts using Distance Preservation Quality and Linear Assignment Sorting](https://arxiv.org/abs/2205.04255v2) from 2022 as sorting algorithm.

Made by [pixolution GmbH, Berlin](https://pixolution.org) as part of the [xCurator research project](https://www.landesmuseum.de/digital/projekte-museum-der-zukunft/kuenstliche-intelligenz-museum/ki-pilotin-werden). The [embeddings-grid](https://github.com/pixolution/embeddings-grid) project code is available under the Apache License 2.0. See the [LICENSE](./LICENSE) file for more info.

<img src="./examples/BLM_sorted_color_shape.jpeg" width="400" height="400" />

## Getting started
The API is defined in `openapi/openapi.yaml`. [OpenAPI Generator](https://plugins.gradle.org/plugin/org.openapi.generator) is used to generate spring boot 2.7.6 java source code (spring 5.3.24).



#### Re-generate spring java source from openapi specification
```
./gradlew --warning-mode all openApiGenerate
```
* To directly generate and copy the sources
```
./renew_openapi_from_spec.sh
```

#### Using the project
* Start local test server
```
./gradlew bootRun
```

* view api documentation: http://127.0.0.1:8080
* do a test query using curl and `examples/request.json` file
```
curl -X 'POST' \
  'http://127.0.0.1:8080/sort' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d @examples/request.json
```

* Run tests
```
./gradlew test
```

* Build and run production docker image (requires docker locally installed)
```
./gradlew docker
docker run -p8080:8080 embeddings-grid:latest
```

## Examples
The `examples/` contains a response and request json example and a HTML visualization of the results. The embedding used in the examples is a color embedding developed by [pixolution GmbH, Berlin](https://pixolution.org) and is not part of this project.
 * [JSON request](./examples/response.json) example using ~1000 [BLM](https://www.landesmuseum.de/digital) images and the corresponding [JSON response](./examples/request.json)
 * [HTML visualization of the response](./examples/html_urls/BLM_sorted_color_shape.html) that uses URLs to https://expotest.bsz-bw.de/blm/digitaler-katalog/
 * [HTML visualization of the response rendered as JPG image](./examples/BLM_sorted_color_shape.jpeg)
