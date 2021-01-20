package com.arondor.arender.sample.connector;

import com.arondor.viewer.annotation.api.Annotation;
import com.arondor.viewer.annotation.api.BookmarkElemType;
import com.arondor.viewer.annotation.api.RotationElemType;
import com.arondor.viewer.annotation.common.AnnotationFlags;
import com.arondor.viewer.annotation.exceptions.AnnotationsNotSupportedException;
import com.arondor.viewer.annotation.exceptions.InvalidAnnotationFormatException;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.common.usercontext.UserContextHolder;
import com.arondor.viewer.rendition.api.document.DocumentAccessor;
import com.arondor.viewer.rendition.api.document.DocumentService;
import com.arondor.viewer.xfdf.annotation.SerializedAnnotationContent;
import com.arondor.viewer.xfdf.annotation.XFDFAnnotationAccessor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Using this AnnotationAccessor will make annotation only editable by the creator of the annotation itself
 */
public class SampleRestrictedXFDFAnnotationAccessor extends XFDFAnnotationAccessor
{
    private static final Logger LOGGER = Logger.getLogger(SampleRestrictedXFDFAnnotationAccessor.class);

    private DocumentId documentId;

    public SampleRestrictedXFDFAnnotationAccessor(DocumentService documentService, DocumentId documentId)
    {
        super(documentService, documentId);
        this.documentId = documentId;
    }

    public SampleRestrictedXFDFAnnotationAccessor(DocumentService documentService, DocumentAccessor documentAccessor)
    {
        super(documentService, documentAccessor);
        this.documentId = documentAccessor.getDocumentId();
    }

    @Override
    public synchronized List<Annotation> get() throws AnnotationsNotSupportedException, InvalidAnnotationFormatException
    {
        if (this.documentId == null)
        {
            throw new IllegalArgumentException("Invalid null documentId provided !");
        }
        LOGGER.debug("Getting all annotations for document: " + documentId);
        List<Annotation> annotations = new ArrayList<Annotation>();

        Collection<SerializedAnnotationContent> allContents = getContentAccessor().getAll(documentId);
        LOGGER.debug("=> Annotations come from " + allContents.size() + " different sources.");

        for (SerializedAnnotationContent content : allContents)
        {
            List<Annotation> parsedAnnotations = getAnnotationConverter().parse(documentId, content.get());
            setAnnotationsLockedAndReadOnly(parsedAnnotations);
            annotations.addAll(parsedAnnotations);
        }
        return annotations;
    }

    /**
     * Disable the right to modify and remove annotations if the current user is not the creator
     *
     * @param annotations
     */
    private void setAnnotationsLockedAndReadOnly(List<Annotation> annotations)
    {
        for (Annotation annotation : annotations)
        {
            if(!annotation.getCreator().equals(UserContextHolder.getUserContext().getUsername()))
            {
                setAnnotationLockedAndReadOnly(annotation);
            }
        }
    }

    /**
     * Disable the right to modify and remove an annotation expect
     * Rotations and Bookmarks
     *
     * @param annotation
     */
    private void setAnnotationLockedAndReadOnly(Annotation annotation)
    {
        if (!(annotation instanceof RotationElemType) && !(annotation instanceof BookmarkElemType))
        {
            if (annotation.getFlags() == null)
            {
                annotation.setFlags(new AnnotationFlags());
            }
            annotation.getFlags().setLocked(true);
            annotation.getFlags().setReadonly(true);
        }
    }
}
