package com.arondor.arender.sample.connector.authentication.service;

import com.arondor.viewer.client.api.context.UserContext;
import com.arondor.viewer.client.api.document.DocumentId;
import com.arondor.viewer.common.document.authentication.service.AuthenticationServiceProvider;
import com.arondor.viewer.common.usercontext.UserContextHolder;
import com.arondor.viewer.rendition.api.document.DocumentService;

public class CustomAuthenticationServiceProvider implements AuthenticationServiceProvider
{
    private static final String AUTHORIZED_USER = "customUser";

    /**
     * Method that calculates if the the current document can be redacted
     * @param documentService
     * @param documentId
     * @return true if redaction is authorized, false otherwise
     */
    public boolean isAuthorized(DocumentService documentService, DocumentId documentId)
    {
        UserContext userContext = UserContextHolder.getUserContext();
        if (userContext != null)
        {
            return AUTHORIZED_USER.equalsIgnoreCase(userContext.getUsername());
        }
        return false;
    }

}
