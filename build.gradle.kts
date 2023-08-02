import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.2"
    id("org.openapi.generator").version("7.0.0-beta")
    id("org.springframework.boot").version("2.7.6")
    id("com.palantir.docker").version("0.35.0")
}


springBoot {
	mainClass.set("de.pixolution.embeddingsGrid.invoker.OpenApiGeneratorApplication")
}

java {
    group = "de.pixolution.embeddingsGrid"
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}

var appVer = "0.9.0"
val springboot_version="2.7.6"
val springdoc_version="1.6.14"
val swagger_ui_version="4.15.5"


dependencies {
    // used by SOM algorithm implementation
    implementation("it.unimi.dsi:fastutil:8.5.6")
    // spring boot environment
    implementation("org.springframework.boot:spring-boot-starter-web:${springboot_version}")
	  implementation("org.springframework.data:spring-data-commons:${springboot_version}")
   	implementation("org.springframework.boot:spring-boot-starter-validation:${springboot_version}")
   	implementation("org.springdoc:springdoc-openapi-ui:${springdoc_version}")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    // for proper escape of ECMA/JSON in tests
    testImplementation("org.apache.commons:commons-text:1.10.0")


	// testing for spring application
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springboot_version}")
    // include all jars in libs/ folder for tests
    //testImplementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

// add the openapi generated source folder to main source set
java.sourceSets["main"].java {
    srcDir("src/openapi/java")
}

val spec = "$rootDir/openapi/openapi.yaml"
val generatedSourcesDir = "$rootDir/generated_openapi/"

// For openApiGenerate see https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-gradle-plugin/README.adoc
// For configOptions see: https://openapi-generator.tech/docs/generators/spring/
openApiGenerate {
    generatorName.set("spring")

    inputSpec.set(spec)
    outputDir.set(generatedSourcesDir)

    apiPackage.set("de.pixolution.embeddingsGrid.api")
    invokerPackage.set("de.pixolution.embeddingsGrid.invoker")
    modelPackage.set("de.pixolution.embeddingsGrid.model")
  	modelNameSuffix = "Json"
    configOptions.set(mapOf(
            "dateLibrary" to "java17",
            "delegatePattern" to "true",
            "developerEmail" to  "info@pixolution.de",
		    "developerName" to  "Sebastian Lutter",
		    "developerOrganization" to  "pixolution GmbH Berlin",
		    "developerOrganizationUrl" to  "https://pixolution.org",
		    "hideGenerationTimestamp" to "true",
		    "dateLibrary" to  "java.util.Date",
		    "useJakartaEe" to "false",
		    "unhandledException" to "false",
    ))
}



val bootJar: BootJar by tasks

tasks {
  docker {
      name = "${project.name}"
      files(bootJar.get().archiveFile)
      noCache(true)
      dependsOn(bootJar.get())
  }
}
