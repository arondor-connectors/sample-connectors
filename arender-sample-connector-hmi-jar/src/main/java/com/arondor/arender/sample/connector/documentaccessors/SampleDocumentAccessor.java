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

    private String myURLParam;

    private String documentTitle;

    private AnnotationAccessor annotationAccessor;

    private final DocumentMetadata documentMetadata = new DocumentMetadata();

    public SampleDocumentAccessor(String myURLParam, DocumentId documentId)
    {
        this.documentId = documentId;
        this.myURLParam = myURLParam;
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
        return "ARender-doc-demo.pdf";
    }

    public void setDocumentTitle(String documentTitle)
    {
        this.documentTitle = documentTitle;
    }

    /**
     * This is where the document has to be fetched with the parameter(s) extracted from the URL and potentially
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException
    {
        // Make the call to the service that able ARender to fetch the document.
        String mySampleRemoteDocument = "https://arender.io/docs/ARender-doc-demo.pdf";
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
