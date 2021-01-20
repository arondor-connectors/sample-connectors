package com.arondor.arender.sample.connector;

import com.arondor.viewer.annotation.api.Annotation;
import com.arondor.viewer.annotation.exceptions.AnnotationsNotSupportedException;
import com.arondor.viewer.annotation.exceptions.InvalidAnnotationFormatException;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.xfdf.annotation.SerializedAnnotationContent;
import com.arondor.viewer.xfdf.annotation.SerializedAnnotationContentAccessor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class to use only if all XFDF annotations of one document are saved in the same inputStream
 * and specific call needs to be done to get/update annotation
 */
public class SampleSerializedAnnotationContentAccessor implements SerializedAnnotationContentAccessor
{
    private static final Logger LOGGER = Logger.getLogger(SampleSerializedAnnotationContentAccessor.class);

    @Override public Collection<SerializedAnnotationContent> getAll(DocumentId documentId)
            throws AnnotationsNotSupportedException, InvalidAnnotationFormatException
    {
        LOGGER.debug("getAll annotations for documentId: " + documentId);
        List<SerializedAnnotationContent> annotations = new ArrayList<SerializedAnnotationContent>();
        annotations.add(new SampleSerializedAnnotationContent(documentId));
        return annotations;
    }

    @Override public SerializedAnnotationContent getForModification(DocumentId documentId, Annotation annotation)
            throws AnnotationsNotSupportedException, InvalidAnnotationFormatException
    {
        LOGGER.debug("get annotations for documentId: " + documentId);
        return new SampleSerializedAnnotationContent(documentId);
    }

}
