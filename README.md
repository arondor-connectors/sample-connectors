# Connector samples

## Introduction

This project gives example on how to build connector for ARender. Please respect the product requirements before testing
it: https://arender.io/documentation/install/standalone/

## Context

In ARender's world a connector is describing how to fetch documents, annotations, metadata. More information on our
document: https://arender.io/documentation/connector/custom/.

ARender has two modules:

* HMI module: Web application
* Rendition module: Standalone JVM

Both of these modules can have connectors.

## Connector implementation

### Connector

Maven structure:

* A parent module: arender-sample-hmi
    * A child module: arender-sample-hmi-connector: create a jar that is the connector,
    * A child module: arender-sample-hmi-war: Fetch ARender HMI WAR, modify it by including the jar above in its
      lib (overlay).

* Development key classes: 
    * SampleURLParser.java: Extract URL parameters from this class
    * SampleDocumentAccessor.java: Document fetching describer
    * SampleAnnotationAccessor.java: Annotation CRUD

## Connector Deployment

### On which module should I develop my connector?

#### Should I deploy the connector on HMI side?

Pros&cons to have the connector on HMI side:

* Pros:
    * Packaging: it is simpler to package an additional jar on a Web-Application
    * Rely on the application server authentication: It can be needed to let the application server handle the
      authentication process
* Cons:
    * Document is transferred from the document repository to the HMI and then to the Rendition. Even, if it is
      transferred by part it is an additional overhead.

#### Should I deploy the connector on Rendition side?

Pros&cons to have the connector on HMI side:

* Pros:
    * Document is directly transferred from the document repository to the Rendition
* Cons:
    * Packaging a little more complicated: It has to be done after the installation of the Rendition

### Deployment

#### Deploy the connector on HMI side

You can either:
* Build the arender-sample-hmi module with Maven,
* Build the war manually:
    * Connector jar should be placed in the WEB-INF/lib of ARender WAR,
    * Copy the  file [arender-custom-server-integration.xml](/arender-sample-hmi/arender-sample-hmi-war/src/main/resources/arender-custom-integration.xml) in ARender WAR WEB-INF/classes
    * Modify the file [arender-custom-server-integration.xml](/arender-sample-hmi/arender-sample-hmi-war/src/main/resources/arender-server.properties) in ARender WAR WEB-INF/classes

#### To test

* Start the RenditionEngine
* Start the Application Server
* Load in your browser one of the URL below:
    * http://localhost:8080/arender-sample-hmi-war-1.0-SNAPSHOT/?myURLParam=ARender-2019.pdf
    * http://localhost:8080/arender-sample-hmi-war-1.0-SNAPSHOT/?myURLParam=ARender-2019.pdf&myURLParam=mail-arender.eml

#### Deploy the connector on Rendition side

* Build the whole project module with maven
* Then copy the folder target/rendition-engine-package-sample in a directory closed to the root folder of your OS.
* Then start ARender using ARenderConsole script in the root folder of the Rendition folder

#### To test

* Build the arender-sample-connector-rendition-jar module and copy the jar file in the folder
  rendition-engine-package-4.X.Y\modules\RenditionEngine\client_libs
* Load in your browser the
  URL: http://localhost:8080/arender-sample-hmi-war-1.0-SNAPSHOT/?bean=sampleDocumentAccessorProxy&documentTitle=MyDocumentTitle
  
#### Annotation Fetching
Annotation are by default managed by the HMI. 
To configure the annotation management by the Rendition follow the below procedure (assuming the SampleAnnotationAccessor HMI class should be used)

Configuration tips

* On HMI side disable the annotation management: uncomment the **delegate** bean in **arender-custom-server-integration.xml** to override useLocalDocumentAccessorForAnnotations value
* On Rendition side
    * Configure the annotation accessor beans: in **custom-integration.xml** uncomment the beans sampleAnnotationAccessorFactory and sampleAnnotationAccessor
    * Configure the AnnotationAccessorFactory: in **application.properties** uncomment the property arender.external.annotation.accessor.factory.bean.name
    * Add **arender-sample-hmi-connector** library on the **RenditionEngine/client_libs** folder (or use the maven build by uncommenting the related build)

To test:
* Load in your browser the
  URL: http://localhost:8080/arender-sample-hmi-war-1.0-SNAPSHOT/?bean=sampleAnnotationAccessorProxy&documentTitle=MyDocumentTitle