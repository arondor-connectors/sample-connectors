package com.arondor.viewer;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.arondor.viewer.server.configuration.ServletConfigurationService;
import com.arondor.viewer.server.servlet.AlterDocumentContentServiceImpl;
import com.arondor.viewer.server.servlet.AnnotationServiceServlet;
import com.arondor.viewer.server.servlet.ArondorViewerServiceImpl;
import com.arondor.viewer.server.servlet.CompositeAccessorServlet;
import com.arondor.viewer.server.servlet.CropImageServlet;
import com.arondor.viewer.server.servlet.DownloadDocumentWithCompareResultsServlet;
import com.arondor.viewer.server.servlet.DownloadServlet;
import com.arondor.viewer.server.servlet.ExceptionHandlerServlet;
import com.arondor.viewer.server.servlet.HealthRecordsServlet;
import com.arondor.viewer.server.servlet.ImageServlet;
import com.arondor.viewer.server.servlet.MergeDocumentsServlet;
import com.arondor.viewer.server.servlet.PrintServlet;
import com.arondor.viewer.server.servlet.PrometheusMetricsServlet;
import com.arondor.viewer.server.servlet.ServletCSVAnnotations;
import com.arondor.viewer.server.servlet.ServletXFDFAnnotations;
import com.arondor.viewer.server.servlet.StreamingServlet;
import com.arondor.viewer.server.servlet.TokenValidatorServlet;
import com.arondor.viewer.server.servlet.UploadServlet;
import com.arondor.viewer.server.servlet.WeatherServlet;
import com.arondor.viewer.server.servlet.jsp.DestroySessionServlet;
import com.arondor.viewer.server.servlet.jsp.DocumentLayoutServlet;
import com.arondor.viewer.server.servlet.jsp.DownloadBase64EncodedDocumentServlet;
import com.arondor.viewer.server.servlet.jsp.DownloadDocumentWithAnnotationsServlet;
import com.arondor.viewer.server.servlet.jsp.EvictDocumentServlet;
import com.arondor.viewer.server.servlet.jsp.FlatDocumentLayoutServlet;
import com.arondor.viewer.server.servlet.jsp.OpenExternalDocumentServlet;
import com.arondor.viewer.server.servlet.jsp.PageContentServlet;
import com.arondor.viewer.server.servlet.jsp.PrintPageServlet;
import com.arondor.viewer.server.servlet.jsp.UpdateCurrentDocumentPropertiesServlet;
import com.arondor.viewer.server.servlet.jsp.UpdateDocumentMetadataServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

@PropertySource(ignoreResourceNotFound = true, value = { "classpath:arender-custom-server.properties",
        "classpath:arender-servlet-configuration-default.properties",
        "classpath:arender-servlet-configuration.properties" })
@DependsOn("arenderLoaded")
@Configuration
@Component
public class ARenderServletConfiguration
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ARenderServletConfiguration.class);

    /*
     * The GWT resources path in the final JAR package
     */
    private static final String GWT_BASE_PATH = "/public/arendergwt/";

    @Value("${servlet.composite.cache.duration.ms}")
    private String compositeCacheDurationMs = "3600000";

    @Value("${servlet.async.supported}")
    private boolean servletAsyncSupported = false;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Bean
    ServletRegistrationBean<ServletConfigurationService> configurationServiceServlet()
    {
        ServletConfigurationService servlet = new ServletConfigurationService()
        {
            private static final long serialVersionUID = -903355888528731441L;

            @Override
            protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
                    String strongName)
            {
                return buildSerializationPolicy(request, moduleBaseURL, strongName);
            }
        };
        return buildForGWT(servlet, "configurationService");
    }

    @Bean
    ServletRegistrationBean<ArondorViewerServiceImpl> arondorViewerServiceServlet()
    {
        ArondorViewerServiceImpl servlet = new ArondorViewerServiceImpl()
        {
            private static final long serialVersionUID = 5772645480734190550L;

            @Override
            protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
                    String strongName)
            {
                return buildSerializationPolicy(request, moduleBaseURL, strongName);
            }
        };
        return buildForGWT(servlet, "arondorViewerService");
    }

    @Bean
    ServletRegistrationBean<AnnotationServiceServlet> annotationServiceServlet()
    {
        AnnotationServiceServlet servlet = new AnnotationServiceServlet()
        {
            private static final long serialVersionUID = -390599094994791629L;

            @Override
            protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
                    String strongName)
            {
                return buildSerializationPolicy(request, moduleBaseURL, strongName);
            }

        };
        return buildForGWT(servlet, "annotationService");
    }

    @Bean
    ServletRegistrationBean<AlterDocumentContentServiceImpl> alterDocumentContentService()
    {
        AlterDocumentContentServiceImpl servlet = new AlterDocumentContentServiceImpl()
        {
            private static final long serialVersionUID = 2082364836065155073L;

            @Override
            protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
                    String strongName)
            {
                return buildSerializationPolicy(request, moduleBaseURL, strongName);
            }

        };
        return buildForGWT(servlet, "alterDocumentContentService");
    }

    @Bean
    ServletRegistrationBean<ExceptionHandlerServlet> exceptionHandlerServlet()
    {
        ExceptionHandlerServlet servlet = new ExceptionHandlerServlet();
        return buildForGWT(servlet, "exceptionHandlerServlet");
    }

    @Bean
    ServletRegistrationBean<ImageServlet> imageServlet()
    {
        ImageServlet servlet = new ImageServlet();
        return buildForGWT(servlet, "imageServlet", "imageServletSVG");
    }

    @Bean
    ServletRegistrationBean<CropImageServlet> cropImageServlet()
    {
        CropImageServlet servlet = new CropImageServlet();
        return buildForGWT(servlet, "cropImageServlet");
    }

    @Bean
    ServletRegistrationBean<PrintServlet> printServlet()
    {
        PrintServlet servlet = new PrintServlet();
        return buildForGWT(servlet, "printServlet");
    }

    @Bean
    ServletRegistrationBean<MergeDocumentsServlet> mergeDocumentsServlet()
    {
        MergeDocumentsServlet servlet = new MergeDocumentsServlet();
        return buildForGWT(servlet, "mergeDocumentsServlet");
    }

    @Bean
    ServletRegistrationBean<DownloadServlet> arenderDownloadServlet()
    {
        DownloadServlet servlet = new DownloadServlet();
        return buildForGWT(servlet, "downloadServlet");
    }

    @Bean
    ServletRegistrationBean<ServletXFDFAnnotations> servletXFDFAnnotations()
    {
        ServletXFDFAnnotations servlet = new ServletXFDFAnnotations();
        return buildForGWT(servlet, "servletXFDFAnnotations");
    }

    @Bean
    ServletRegistrationBean<StreamingServlet> streamingServlet()
    {
        StreamingServlet servlet = new StreamingServlet();
        return buildForGWT(servlet, "streamingServlet");
    }

    @Bean
    ServletRegistrationBean<WeatherServlet> weatherServlet()
    {
        WeatherServlet servlet = new WeatherServlet();
        return buildForGWT(servlet, "weather");
    }

    @Bean
    ServletRegistrationBean<HealthRecordsServlet> healthRecordsServlet()
    {
        HealthRecordsServlet servlet = new HealthRecordsServlet();
        return buildForGWT(servlet, "health/records");
    }

    @Bean
    ServletRegistrationBean<DownloadDocumentWithAnnotationsServlet> downloadDocumentWithAnnotationsServlet()
    {
        DownloadDocumentWithAnnotationsServlet servlet = new DownloadDocumentWithAnnotationsServlet();
        return buildForGWT(servlet, "downloadDocumentWithAnnotations");
    }

    @Bean
    ServletRegistrationBean<EvictDocumentServlet> evictDocumentServlet()
    {
        EvictDocumentServlet servlet = new EvictDocumentServlet();
        return buildForGWT(servlet, "evictDocument");
    }

    @Bean
    ServletRegistrationBean<DocumentLayoutServlet> documentLayoutServlet()
    {
        DocumentLayoutServlet servlet = new DocumentLayoutServlet();
        return buildForGWT(servlet, "documentLayout");
    }

    @Bean
    ServletRegistrationBean<UpdateDocumentMetadataServlet> updateDocumentMetadataServlet()
    {
        UpdateDocumentMetadataServlet servlet = new UpdateDocumentMetadataServlet();
        return buildForGWT(servlet, "updateDocumentMetadataServlet");
    }

    @Bean
    ServletRegistrationBean<DownloadDocumentWithCompareResultsServlet> downloadDocumentWithCompareResultsServlet()
    {
        DownloadDocumentWithCompareResultsServlet servlet = new DownloadDocumentWithCompareResultsServlet();
        return buildForGWT(servlet, "downloadServlet/mergedWithCompareResult");
    }

    @Bean
    ServletRegistrationBean<ServletCSVAnnotations> servletCSVAnnotations()
    {
        ServletCSVAnnotations servlet = new ServletCSVAnnotations();
        return buildForGWT(servlet, "servletCSVAnnotations");
    }

    @Bean
    ServletRegistrationBean<FlatDocumentLayoutServlet> flatDocumentLayoutServlet()
    {
        FlatDocumentLayoutServlet servlet = new FlatDocumentLayoutServlet();
        return buildForGWT(servlet, "flatDocumentLayout");
    }

    @Bean
    ServletRegistrationBean<UploadServlet> uploadServlet()
    {
        UploadServlet servlet = new UploadServlet();
        return buildForGWT(servlet, "uploadServlet");
    }

    @Bean
    ServletRegistrationBean<TokenValidatorServlet> tokenValidatorServlet()
    {
        TokenValidatorServlet servlet = new TokenValidatorServlet();
        return buildForGWT(servlet, "validateToken");
    }

    @Bean
    ServletRegistrationBean<CompositeAccessorServlet> compositeAccessorServlet()
    {
        CompositeAccessorServlet servlet = new CompositeAccessorServlet(compositeCacheDurationMs);
        return buildForGWT(servlet, "compositeAccessorServlet");
    }

    @Bean
    ServletRegistrationBean<DownloadBase64EncodedDocumentServlet> downloadBase64EncodedDocument()
    {
        DownloadBase64EncodedDocumentServlet servlet = new DownloadBase64EncodedDocumentServlet();
        return buildForGWT(servlet, "downloadBase64EncodedDocument");
    }

    @Bean
    ServletRegistrationBean<DestroySessionServlet> destroySession()
    {
        DestroySessionServlet servlet = new DestroySessionServlet();
        return buildForGWT(servlet, "destroySession");
    }

    @Bean
    ServletRegistrationBean<PageContentServlet> pageContent()
    {
        PageContentServlet servlet = new PageContentServlet();
        return buildForGWT(servlet, "pageContent");
    }

    @Bean
    ServletRegistrationBean<OpenExternalDocumentServlet> openExternalDocument()
    {
        OpenExternalDocumentServlet servlet = new OpenExternalDocumentServlet();
        return buildForGWT(servlet, "openExternalDocument");
    }

    @Bean
    ServletRegistrationBean<UpdateCurrentDocumentPropertiesServlet> updateCurrentDocumentProperties()
    {
        UpdateCurrentDocumentPropertiesServlet servlet = new UpdateCurrentDocumentPropertiesServlet();
        return buildForGWT(servlet, "updateCurrentDocumentProperties");
    }

    @Bean
    ServletRegistrationBean<PrometheusMetricsServlet> prometheusMetricsServlet()
    {
        PrometheusMetricsServlet servlet = new PrometheusMetricsServlet();
        return buildForGWT(servlet, "prometheus");
    }

    @Bean
    ServletRegistrationBean<PrintPageServlet> printPage()
    {
        PrintPageServlet servlet = new PrintPageServlet();
        return buildForGWT(servlet, "printPage");
    }

    private <T extends Servlet> ServletRegistrationBean<T> buildForGWT(T servlet, String... names)
    {
        String[] mappings = new String[names.length];
        for (int i = 0; i < names.length; i++)
        {
            mappings[i] = "/arendergwt/" + names[i];
            if ("health".equals(names[i]))
            {
                mappings[i] = "/" + names[i];
            }
        }
        ServletRegistrationBean<T> bean = new ServletRegistrationBean<>(servlet, mappings);
        beanFactory.autowireBean(servlet);
        bean.setLoadOnStartup(1);
        bean.setAsyncSupported(servletAsyncSupported);
        return bean;
    }

    private SerializationPolicy buildSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
            String strongName)
    {
        String moduleBaseURLHdr = request.getHeader("X-GWT-Module-Base");
        if (moduleBaseURLHdr != null)
        {
            moduleBaseURL = moduleBaseURLHdr;
        }
        LOGGER.trace("Request={}, moduleBaseURL={}, strongName={}", request.getContextPath(), moduleBaseURL,
                strongName);

        return loadSerializationPolicy(strongName);
    }

    private SerializationPolicy loadSerializationPolicy(String strongName)
    {
        String serializationPolicyStrongName = GWT_BASE_PATH + strongName;
        String serializationPolicyFilePath = SerializationPolicyLoader
                .getSerializationPolicyFileName(serializationPolicyStrongName);

        try (InputStream is = this.getClass().getResourceAsStream(serializationPolicyFilePath))
        {
            if (is != null)
            {
                try
                {
                    SerializationPolicy serializationPolicy = SerializationPolicyLoader.loadFromStream(is, null);
                    return serializationPolicy;
                }
                catch (ParseException e)
                {
                    LOGGER.error("ERROR: Failed to parse the policy file '" + serializationPolicyFilePath + "'", e);
                    throw new RuntimeException(
                            "ERROR: Failed to parse the policy file '" + serializationPolicyFilePath + "'", e);
                }
                catch (IOException e)
                {
                    LOGGER.error("ERROR: Could not read the policy file '" + serializationPolicyFilePath + "'", e);
                    throw new RuntimeException(
                            "ERROR: Failed to parse the policy file '" + serializationPolicyFilePath + "'", e);
                }
            }
            String message = "ERROR: The serialization policy file '" + serializationPolicyFilePath
                    + "' was not found; did you forget to include it in this deployment?";

            LOGGER.error(message);
            throw new RuntimeException("ERROR: Failed to parse the policy file '" + serializationPolicyFilePath + "'");
        }
        catch (IOException e)
        {
            LOGGER.error("ERROR: Could not read the policy file '" + serializationPolicyFilePath + "'", e);
            throw new RuntimeException("ERROR: Failed to parse the policy file '" + serializationPolicyFilePath + "'",
                    e);
        }
    }
}
