package com.arondor.viewer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.arondor.viewer.common.document.statistics.DocumentStatisticsProvider;
import com.arondor.viewer.common.util.DependencyInjection;
import com.arondor.viewer.server.servlet.ServletDocumentService;
import com.arondor.viewer.server.servlet.utils.ARenderServletConfiguration;

@Configuration
@Import({ ARenderServletConfiguration.class })
public class ARenderAutoConfiguration
{
    private static Logger LOGGER = Logger.getLogger(ARenderAutoConfiguration.class);

    @Bean
    List<Object> arenderLoaded(AutowireCapableBeanFactory beanFactory)
    {
        overrideSpringContext(beanFactory);
        ServletDocumentService.getInstance();
        DocumentStatisticsProvider.getInstance();
        return new ArrayList<Object>();
    }

    private void changeDependencyInjectionInstance()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        DependencyInjectionImpl di = new DependencyInjectionImpl();
        Field declaredField = DependencyInjection.class.getDeclaredField("instance");
        declaredField.setAccessible(true);
        declaredField.set(DependencyInjectionImpl.class, di);
        declaredField.setAccessible(false);
    }

    public void overrideSpringContext(AutowireCapableBeanFactory beanFactory)
    {
        try
        {
            changeDependencyInjectionInstance();

            Field declaredField = DependencyInjection.class.getDeclaredField("context");
            boolean accessible = declaredField.isAccessible();

            declaredField.setAccessible(true);

            DependencyInjection obj = DependencyInjection.getInstance();

            ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext()
            {
                @Override
                public Object getBean(String name) throws BeansException
                {
                    return beanFactory.getBean(name);
                }

                @Override
                public Object getBean(String name, Object... args) throws BeansException
                {
                    return beanFactory.getBean(name, args);
                }

                @Override
                public void refresh() throws BeansException, IllegalStateException
                {
                    // actual.refresh();
                }

                @Override
                public void stop()
                {
                    // actual.stop();
                }
            };

            declaredField.set(obj, c);
            declaredField.setAccessible(accessible);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            LOGGER.error("Error while overriding spring context", e);
        }
    }
}
