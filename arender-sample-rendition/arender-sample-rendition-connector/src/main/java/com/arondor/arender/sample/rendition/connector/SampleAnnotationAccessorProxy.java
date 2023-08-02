package com.arondor.arender.sample.rendition.connector;

import com.arondor.viewer.client.api.document.DocumentFormatNotSupportedException;
import com.arondor.viewer.client.api.document.DocumentLayout;
import com.arondor.viewer.client.api.document.DocumentNotAvailableException;
import com.arondor.viewer.rendition.api.annotation.AnnotationAccessor;
import com.arondor.viewer.rendition.api.document.ExternalAnnotationAccessorProxy;

import java.io.IOException;
import java.util.Map;

/**
 * Use this class to enabled the annotation and document management from the Rendition side
 */
public class SampleAnnotationAccessorProxy extends SampleDocumentAccessorProxy
        implements ExternalAnnotationAccessorProxy
{
    /**
     * Method that details how to fetch annotations
     * (by default should return null because it is handled by the Rendition Engine property
     * arender.external.annotation.accessor.factory.bean.name)
     * @param beanName: the bean name passed as value of bean parameter from the URL
     * @param uniqueId: the ARender id of the document (UUID)
     * @param properties: the list of properties coming from ARender HMI (by default it is all parameter from the URL)
     * @return the document to show as InputStream
     */
    @Override public AnnotationAccessor getAnnotationAccessor(String beanName, String uniqueId,
            Map<String, String> properties)
    {
        return null;
    }
}
