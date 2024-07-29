package com.arondor.arender.sample.rendition.connector;

import com.arondor.viewer.client.api.document.DocumentFormatNotSupportedException;
import com.arondor.viewer.client.api.document.DocumentLayout;
import com.arondor.viewer.client.api.document.DocumentNotAvailableException;
import com.arondor.viewer.rendition.api.document.ExternalDocumentAccessorProxy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class SampleDocumentAccessorProxy implements ExternalDocumentAccessorProxy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleDocumentAccessorProxy.class);

    /**
     * Method that details how to fetch documents
     * @param beanName: the bean name passed as value of bean parameter from the URL
     * @param uniqueId: the ARender id of the document (UUID)
     * @param properties: the list of properties coming from ARender HMI (by default it is all parameter from the URL)
     * @return the document to show as InputStream
     * @throws IOException Exception to throw in case of error
     */
    @Override public InputStream getDocumentContents(String beanName, String uniqueId, Map<String, String> properties)
            throws IOException
    {
        LOGGER.debug("Will get the document for DocumentId: " + uniqueId);

        // Extract values from the properties Map in order to fetch the document with
//        String id = properties.get("id");
//        String token = properties.get("token");
//        LOGGER.debug("Will fetch the document having the id " + id + " with the token " + token);
        // Make the call to the service that enable ARender Rendition to fetch the document.
//        InputStream myDocument = myService.getDocument(id, token);

        // Below an example for the document to be shown
        String mySampleRemoteDocument = "https://demo.arender.io/docs/demo/PDFReference15_v5.pdf";
        URL url = new URL(mySampleRemoteDocument);
        return url.openStream();
    }

    @Override public byte[] getDocumentContentsByteArray(String beanName, String uniqueId,
            Map<String, String> properties) throws IOException
    {
        return IOUtils.toByteArray(getDocumentContents(beanName, uniqueId, properties));
    }

    @Override
    public DocumentLayout getDocumentLayout(Map<String, String> map)
            throws DocumentFormatNotSupportedException, DocumentNotAvailableException, IOException
    {
        return null;
    }
}
