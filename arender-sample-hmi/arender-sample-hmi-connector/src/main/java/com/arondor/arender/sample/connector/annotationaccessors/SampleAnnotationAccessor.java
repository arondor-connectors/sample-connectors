package com.arondor.arender.sample.connector.annotationaccessors;

import com.arondor.viewer.annotation.api.Annotation;
import com.arondor.viewer.annotation.exceptions.AnnotationCredentialsException;
import com.arondor.viewer.annotation.exceptions.AnnotationNotAvailableException;
import com.arondor.viewer.annotation.exceptions.AnnotationsNotSupportedException;
import com.arondor.viewer.annotation.exceptions.InvalidAnnotationFormatException;
import com.arondor.viewer.client.api.annotation.AnnotationCreationPolicy;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.client.api.pagesselection.PagesRange;
import com.arondor.viewer.rendition.api.annotation.AnnotationConverter;
import com.arondor.viewer.rendition.api.annotation.AnnotationPageAccessor;
import com.arondor.viewer.rendition.api.document.DocumentAccessor;
import com.arondor.viewer.rendition.api.document.DocumentService;
import com.arondor.viewer.xfdf.annotation.*;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SampleAnnotationAccessor implements AnnotationPageAccessor
{
    private static final Logger LOGGER = Logger.getLogger(SampleAnnotationAccessor.class);

    private DocumentId documentId;

    private AnnotationConverter annotationConverter;

    private AnnotationCreationPolicy annotationCreationPolicy;

    public SampleAnnotationAccessor()
    {

    }

    /**
     * Default generic constructor, based only on the documentId
     *
     * @param documentId
     *            the documentId of the document we will manage annotations for
     */
    public SampleAnnotationAccessor(DocumentService documentService, DocumentId documentId)
    {
        if (documentId == null)
        {
            throw new IllegalArgumentException("Invalid null documentId provided !");
        }
        this.documentId = documentId;
        AnnotationTransformer annotationTransformer = new AnnotationPositionTransformer(documentService);
        annotationConverter = new XFDFAnnotationConverter(annotationTransformer);
    }

    /**
     * Constructor required by ServletDocumentService default bean instantiator
     *
     * @param documentAccessor
     *            the document accessor to instantiate the
     *            DocumentAnnotationAccessor for
     */
    public SampleAnnotationAccessor(DocumentService documentService, DocumentAccessor documentAccessor)
    {
        this(documentService, documentAccessor.getDocumentId());
    }

    @Override public void create(List<Annotation> annotations)
            throws AnnotationsNotSupportedException, AnnotationCredentialsException, InvalidAnnotationFormatException,
            AnnotationNotAvailableException
    {
        if (annotations.isEmpty())
        {
            return;
        }
        InputStream updatedContent = annotationConverter.serialize(documentId, annotations);
        // call to your API to create annotations
    }

    @Override public void update(List<Annotation> annotations)
            throws AnnotationsNotSupportedException, AnnotationNotAvailableException, AnnotationCredentialsException,
            InvalidAnnotationFormatException
    {
        if (annotations.isEmpty())
        {
            return;
        }
        InputStream updatedContent = annotationConverter.serialize(documentId, annotations);
        // call to your API to update annotations
    }

    @Override public List<Annotation> get()
            throws AnnotationsNotSupportedException, AnnotationCredentialsException, InvalidAnnotationFormatException
    {
        if (documentId == null)
        {
            throw new IllegalArgumentException("Invalid null documentId provided !");
        }
        LOGGER.debug("Getting all annotations for document: " + documentId);
        List<Annotation> annotations = new ArrayList<Annotation>();

        try
        {
            // Replace new FileInputStream("") by your API call to fetch annotations
            InputStream fetchedAnnotations = new FileInputStream("");
            List<Annotation> parsedAnnotations = annotationConverter.parse(documentId, fetchedAnnotations);
            annotations.addAll(parsedAnnotations);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Could not fetch annotations for documentId " + documentId, e);
        }
        return annotations;
    }

    @Override public void delete(List<Annotation> annotations)
            throws AnnotationsNotSupportedException, AnnotationCredentialsException, AnnotationNotAvailableException,
            InvalidAnnotationFormatException
    {
        if (annotations.isEmpty())
        {
            return;
        }
        // Call to your API to delete annotations
    }

    @Override public List<Annotation> get(PagesRange pagesRange)
            throws AnnotationsNotSupportedException, AnnotationCredentialsException, InvalidAnnotationFormatException
    {
        List<Annotation> annotations = get();
        List<Annotation> filteredListToTransfer = new ArrayList<Annotation>();
        for (Annotation annotation : annotations)
        {
            if (annotation.getPage() >= pagesRange.getMinPage() && annotation.getPage() <= pagesRange.getMaxPage())
            {
                filteredListToTransfer.add(annotation);
            }
        }
        return filteredListToTransfer;
    }

    @Override public List<Annotation> updateAnnotations(List<Annotation> list, List<Annotation> list1,
            List<Annotation> list2, PagesRange pagesRange)
            throws AnnotationsNotSupportedException, AnnotationCredentialsException, InvalidAnnotationFormatException
    {
        // for now, the mechanism is not ready but can be improved later on
        throw new AnnotationsNotSupportedException();
    }

    @Override public AnnotationCreationPolicy getAnnotationCreationPolicy() throws AnnotationsNotSupportedException
    {
        return annotationCreationPolicy;
    }

    public void setAnnotationCreationPolicy(AnnotationCreationPolicy annotationCreationPolicy)
    {
        this.annotationCreationPolicy = annotationCreationPolicy;
    }

    public AnnotationConverter getAnnotationConverter()
    {
        return annotationConverter;
    }

    public void setAnnotationConverter(AnnotationConverter annotationConverter)
    {
        this.annotationConverter = annotationConverter;
    }
}
