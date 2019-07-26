package com.arondor.arender.sample.connector;

import com.arondor.viewer.client.api.document.DocumentFormatNotSupportedException;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.client.api.document.DocumentNotAvailableException;
import com.arondor.viewer.client.api.document.id.DocumentIdParameter;
import com.arondor.viewer.common.document.id.DocumentIdFactory;
import com.arondor.viewer.common.document.id.URLDocumentIdParameter;
import com.arondor.viewer.rendition.api.DocumentServiceURLParser;
import com.arondor.viewer.rendition.api.document.DocumentAccessor;
import com.arondor.viewer.rendition.api.document.DocumentService;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The ARender Team on 25/07/2019.
 */
public class SampleURLParser implements DocumentServiceURLParser
{
    private static final Logger LOGGER = Logger.getLogger(SampleURLParser.class);

    private static final String MY_URL_REQUEST_PARAMETER = "myURLParam";

    public boolean canParse(DocumentService documentService, javax.servlet.ServletContext servletContext,
            javax.servlet.http.HttpServletRequest httpServletRequest)
    {
        String myURLParam = httpServletRequest.getParameter(MY_URL_REQUEST_PARAMETER);
        return !Strings.isNullOrEmpty(myURLParam);
    }

    public DocumentId parse(DocumentService documentService, javax.servlet.ServletContext servletContext,
            javax.servlet.http.HttpServletRequest httpServletRequest)
            throws DocumentNotAvailableException, DocumentFormatNotSupportedException
    {
        String myURLParam = httpServletRequest.getParameter(MY_URL_REQUEST_PARAMETER);
        List<DocumentIdParameter> parameters = new ArrayList<DocumentIdParameter>();
        parameters.add(new URLDocumentIdParameter(MY_URL_REQUEST_PARAMETER, myURLParam));
        DocumentId documentId = DocumentIdFactory.getInstance().generate(parameters);

        DocumentAccessor documentAccessor = new SampleDocumentAccessor(myURLParam, documentId);
        documentService.loadDocumentAccessor(documentAccessor);
        return documentAccessor.getDocumentId();
    }
}
