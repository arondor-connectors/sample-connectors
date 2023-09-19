package com.arondor.arender.sample.connector.documentaccessors;

import com.arondor.viewer.annotation.exceptions.AnnotationsNotSupportedException;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.client.api.document.metadata.DocumentMetadata;
import com.arondor.viewer.rendition.api.annotation.AnnotationAccessor;
import com.arondor.viewer.rendition.api.document.BinaryDocumentAccessor;
import com.arondor.viewer.rendition.api.document.DocumentAccessor;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by The ARender Team on 26/07/2019.
 */
public class SampleDocumentAccessor implements DocumentAccessor
{
    private static final Logger LOGGER = Logger.getLogger(SampleDocumentAccessor.class);

    private final DocumentId documentId ;

    private final String urlParameterValue;

    private String documentTitle;

    private AnnotationAccessor annotationAccessor;

    private final DocumentMetadata documentMetadata = new DocumentMetadata();

    public SampleDocumentAccessor(String urlParameterValue, DocumentId documentId)
    {
        this.documentId = documentId;
        this.urlParameterValue = urlParameterValue;
    }

    public DocumentId getUUID()
    {
        return this.documentId;
    }

    public DocumentId getDocumentId()
    {
        return this.documentId;
    }

    public String getDocumentTitle()
    {
        return urlParameterValue;
    }

    public void setDocumentTitle(String documentTitle)
    {
        this.documentTitle = documentTitle;
    }

    /**
     * This is where the document has to be fetched with the parameter(s) extracted from the URL and potentially
     * @return The document as InputStream
     * @throws IOException: Exception to return in case of error
     */
    public InputStream getInputStream() throws IOException
    {
        // TODO: Replace the below code by the call to your service with which the document can be downloaded
        // To test the below code use below values for myURLParam:
        // pdf-reference-doc-base.pdf, ARender-2019.pdf, mail-arender.eml
        String mySampleRemoteDocument = "https://demo.arender.io/docs/demo/" + urlParameterValue;
        URL url = new URL(mySampleRemoteDocument);
        return url.openStream();
    }

    public byte[] toByteArray() throws IOException
    {
        return IOUtils.toByteArray(getInputStream());
    }

    public String getPath() throws IOException
    {
        return null;
    }

    public String getMimeType() throws IOException
    {
        return null;
    }

    public AnnotationAccessor getAnnotationAccessor() throws AnnotationsNotSupportedException
    {
        return this.annotationAccessor;
    }

    public void setAnnotationAccessor(AnnotationAccessor annotationAccessor) throws AnnotationsNotSupportedException
    {
        this.annotationAccessor = annotationAccessor;
    }

    public DocumentAccessor asSerializableDocumentAccessor() throws IOException
    {
        return new BinaryDocumentAccessor(this);
    }

    public DocumentMetadata getDocumentMetadata()
    {
        return documentMetadata;
    }
}
