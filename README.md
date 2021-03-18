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

## Connectors. On which module should I develop my connector?

### Connector on HMI side

Pros&cons to have the connector on HMI side:

* Pros:
    * Packaging: it is simpler to package an additional jar on a Web-Application
    * Rely on the application server authentication: It can be needed to let the application server handle the
      authentication process
* Cons:
    * Document is transferred from the document repository to the HMI and then to the Rendition. Even, if it is
      transferred by part it is an additional overhead.

### Connector on Rendition side

Pros&cons to have the connector on HMI side:

* Pros:
    * Document is directly transferred from the document repository to the Rendition
* Cons:
    * Packaging a little more complicated: It has to be done after the installation of the Rendition

## Connector implementation

### Connector on HMI side

Maven structure:

* A parent module: arender-sample-hmi
    * A child module: arender-sample-hmi-connector: create a jar that is the connector,
    * A child module: arender-sample-hmi-war: Fetch ARender HMI WAR, modify it by including the jar above in its
      lib (overlay).

Development entry Point: SampleURLParser.java: Extract URL parameters from this class

To test:

* Build the whole project and deploy the built war file in your application server,
* Load in your browser the URL: http://localhost:8080/arender-sample-hmi-war-1.0-SNAPSHOT/?myURLParam=anyValue

### Connector on Rendition side

Maven structure:

* A parent module: arender-sample-rendition
    * A child module: arender-sample-connector-rendition: create a jar that is the connector,
    * A child module: arender-sample-rendition-package:
      * Fetch ARender Rendition zip, 
      * Modify it by including
        * The configuration file custom-integration.xml located in src/resources
        * The jar above in its lib (overlay)

Development tips: 
* In the method SampleDocumentAccessorProxy.getDocumentContents(String beanName, String uniqueId, Map<String, String> 
  properties), the **properties** parameter will contain all the parameters passed in the URL. 

To test:

* Build the arender-sample-connector-rendition-jar module and copy the jar file in the folder
  rendition-engine-package-4.X.Y\modules\RenditionEngine\client_libs
* Load in your browser the
  URL: http://localhost:8080/arender-sample-hmi-war-1.0-SNAPSHOT/?bean=sampleDocumentAccessorProxy&documentTile=MyDocumentTitle
  
