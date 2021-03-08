package com.arondor.arender.sample.connector.annotationaccessors;

import com.arondor.viewer.annotation.exceptions.AnnotationCredentialsException;
import com.arondor.viewer.annotation.exceptions.AnnotationNotAvailableException;
import com.arondor.viewer.annotation.exceptions.InvalidAnnotationFormatException;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.xfdf.annotation.SerializedAnnotationContent;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Class to use only if all XFDF annotations of one document are saved in the same inputStream
 * and specific call needs to be done to get/update annotation
 */
public class SampleSerializedAnnotationContent implements SerializedAnnotationContent
{
    private static final Logger LOGGER = Logger.getLogger(SampleSerializedAnnotationContent.class);

    private final DocumentId documentId;

    public SampleSerializedAnnotationContent(DocumentId documentId)
    {
        this.documentId = documentId;
    }

    @Override public InputStream get() throws InvalidAnnotationFormatException
    {
        try
        {
            // Replace the new FileInputStream("") by a call to your API to fetch Annotations
            return new FileInputStream("");
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Could not get annotation for documentid " + documentId, e);
        }
        return null;
    }

    @Override public void update(InputStream inputStream)
            throws InvalidAnnotationFormatException, AnnotationCredentialsException, AnnotationNotAvailableException
    {
        if (get() == null)
        {
            // call your API to create annotations in your repository
        }
        else
        {
            // call your API to update annotations in your repository
        }
    }
}
