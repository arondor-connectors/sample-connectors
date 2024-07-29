package com.arondor.arender.sample.connector.annotationaccessors;

import java.util.List;

import org.apache.log4j.Logger;

import com.arondor.viewer.annotation.api.Annotation;
import com.arondor.viewer.annotation.common.AnnotationFlags;
import com.arondor.viewer.annotation.exceptions.AnnotationCredentialsException;
import com.arondor.viewer.annotation.exceptions.AnnotationsNotSupportedException;
import com.arondor.viewer.annotation.exceptions.InvalidAnnotationFormatException;
import com.arondor.viewer.client.api.annotation.AnnotationCreationPolicy;
import com.arondor.viewer.common.usercontext.UserContextHolder;
import com.arondor.viewer.rendition.api.document.DocumentAccessor;
import com.arondor.viewer.rendition.api.document.DocumentService;

public class SampleUserRestrictedAnnotationAccessor extends SampleAnnotationAccessor
{
    private static final Logger LOGGER = Logger.getLogger(SampleAnnotationAccessor.class);

    private List<String> readOnlyUsersList;

    private List<String> disabledAnnotationCreationUsersList;

    public SampleUserRestrictedAnnotationAccessor(DocumentService documentService, DocumentAccessor documentAccessor)
    {
        super(documentService, documentAccessor);
    }

    /**
     * Retrieve annotations and set them in a read-only state if the current user
     * is not in the list of the arender.server.read.only.annotations.user property
     *
     * @throws AnnotationsNotSupportedException
     * @throws AnnotationCredentialsException
     * @throws InvalidAnnotationFormatException
     */
    @Override public List<Annotation> get()
            throws AnnotationsNotSupportedException, AnnotationCredentialsException, InvalidAnnotationFormatException
    {
        List<Annotation> annotations = super.get();
        String userName = UserContextHolder.getUserContext().getUsername();

        if (!readOnlyUsersList.contains(userName))
        {
            return annotations;
        }

        LOGGER.info("Current user is in read only mode");
        for (Annotation annotation : annotations)
        {
            if (annotation.getFlags() == null)
            {
                annotation.setFlags(new AnnotationFlags());
            }
            annotation.getFlags().setReadonly(true);
            annotation.getFlags().setLocked(true);
        }

        return annotations;
    }

    /**
     * Disable the annotation creation if the current user is not in the list of
     * the arender.server.disable.annotations.creation.user property
     *
     * @throws AnnotationsNotSupportedException
     */
    @Override public AnnotationCreationPolicy getAnnotationCreationPolicy() throws AnnotationsNotSupportedException
    {
        AnnotationCreationPolicy annotationCreationPolicy = super.getAnnotationCreationPolicy();
        String userName = UserContextHolder.getUserContext().getUsername();

        if (disabledAnnotationCreationUsersList.contains(userName))
        {
            LOGGER.info("Current user is unable to create annotations");
            annotationCreationPolicy.setCanCreateAnnotations(false);
        }

        return annotationCreationPolicy;
    }

    public void setReadOnlyUsersList(List<String> readOnlyUsersList)
    {
        this.readOnlyUsersList = readOnlyUsersList;
    }

    public void setDisabledAnnotationCreationUsersList(List<String> disabledAnnotationCreationUsersList)
    {
        this.disabledAnnotationCreationUsersList = disabledAnnotationCreationUsersList;
    }
}
