package com.arondor.arender.sample.connector.urlparsers;

import com.arondor.arender.sample.connector.documentaccessors.SampleDocumentAccessor;
import com.arondor.viewer.client.api.document.*;
import com.arondor.viewer.client.api.document.id.DocumentIdParameter;
import com.arondor.viewer.common.document.id.DocumentIdFactory;
import com.arondor.viewer.common.document.id.URLDocumentIdParameter;
import com.arondor.viewer.rendition.api.DocumentServiceURLParser;
import com.arondor.viewer.rendition.api.document.DocumentAccessor;
import com.arondor.viewer.rendition.api.document.DocumentService;
import com.arondor.viewer.server.servlet.utils.DocumentAccessorUtils;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by The ARender Team on 25/07/2019.
 */
public class SampleURLParser implements DocumentServiceURLParser
{
    private static final Logger LOGGER = Logger.getLogger(SampleURLParser.class);

    private static final String MY_URL_REQUEST_PARAMETER = "myURLParam";

    private boolean forceDocumentReferenceMimeTypeDetection = false;

    public boolean canParse(DocumentService documentService, ServletContext servletContext,
            HttpServletRequest httpServletRequest)
    {
        String myURLParam = httpServletRequest.getParameter(MY_URL_REQUEST_PARAMETER);
        if(!Strings.isNullOrEmpty(myURLParam))
        {
            return true;
        }
        else
        {
            String[] multiValuedParameters = httpServletRequest.getParameterValues(MY_URL_REQUEST_PARAMETER);
            if (multiValuedParameters == null)
            {
                return false;
            }
            else
            {
                LOGGER.debug("Multiple document detected, canParse OK");
                for (String multiValuedParameter : multiValuedParameters)
                {
                    if (!Strings.isNullOrEmpty(multiValuedParameter))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public DocumentId parse(DocumentService documentService, ServletContext servletContext,
            HttpServletRequest httpServletRequest)
            throws DocumentNotAvailableException, DocumentFormatNotSupportedException
    {
        String[] urlParameterValueArray = httpServletRequest.getParameterValues(MY_URL_REQUEST_PARAMETER);
        if (urlParameterValueArray.length == 1)
        {
            String urlParameterValue = urlParameterValueArray[0];
            LOGGER.info("Simple document detected with parameter: " + urlParameterValue);
            DocumentAccessor documentAccessor = buildDocumentAccessorAndLoadIt(documentService,
                    urlParameterValue);
            return documentAccessor.getDocumentId();
        }
        else
        {
            LOGGER.info("Multiple documents detected with parameters: " + Arrays.toString(urlParameterValueArray));
            DocumentContainer documentContainer = parseMultipleDocuments(documentService, urlParameterValueArray);
            DocumentAccessorUtils.setDocumentContainerTitle(httpServletRequest, documentContainer);
            return documentContainer.getDocumentId();
        }
    }

    private DocumentContainer parseMultipleDocuments(DocumentService documentService, String[] urlParameterValueArray)
    {
        DocumentContainer documentContainer = DocumentAccessorUtils.buildDocumentContainer(urlParameterValueArray,
                MY_URL_REQUEST_PARAMETER);
        for (String urlParameterValue : urlParameterValueArray)
        {
            LOGGER.info("URL parsed : " + urlParameterValue);
            try
            {
                DocumentAccessor documentAccessor = buildDocumentAccessorAndLoadIt(documentService, urlParameterValue);
                DocumentReference documentReference = DocumentAccessorUtils.buildDocumentReference(documentAccessor,
                        isForceDocumentReferenceMimeTypeDetection());
                documentContainer.getChildren().add(documentReference);
            }
            catch (DocumentNotAvailableException e)
            {
                LOGGER.error("Could not create document accessor for urlParameterValue: " + urlParameterValue + ", " + e.getMessage());
            }
            catch (DocumentFormatNotSupportedException e)
            {
                LOGGER.error("The document cannot be loaded : " + e.getMessage());
            }
            catch (RuntimeException e)
            {
                LOGGER.error("The supplied urlParameterValue should not be valid", e);
            }
        }
        DocumentAccessorUtils.registerDocumentContainer(documentContainer, documentService);
        return documentContainer;
    }

    private DocumentAccessor buildDocumentAccessorAndLoadIt(DocumentService documentService, String urlParameterValue)
            throws DocumentNotAvailableException, DocumentFormatNotSupportedException
    {
        List<DocumentIdParameter> parameters = new ArrayList<>();
        parameters.add(new URLDocumentIdParameter(MY_URL_REQUEST_PARAMETER, urlParameterValue));
        DocumentId documentId = DocumentIdFactory.getInstance().generate(parameters);

        DocumentAccessor documentAccessor = new SampleDocumentAccessor(urlParameterValue, documentId);
        documentService.loadDocumentAccessor(documentAccessor);
        return documentAccessor;
    }

    public boolean isForceDocumentReferenceMimeTypeDetection()
    {
        return forceDocumentReferenceMimeTypeDetection;
    }

    public void setForceDocumentReferenceMimeTypeDetection(boolean forceDocumentReferenceMimeTypeDetection)
    {
        this.forceDocumentReferenceMimeTypeDetection = forceDocumentReferenceMimeTypeDetection;
    }
}
