/**
 * ================================================================
 * Copyright (c) 2020-2021 Maiereni Software and Consulting Inc
 * ================================================================
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maiereni.cad.rmlserver.mvc;

import com.maiereni.cad.rmlserver.bo.ApplicationSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.Jsp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The MvcConfiguration class configures the web layer of the application
 *
 * @author Petre Maierean
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.maiereni.cad.rmlserver.mvc"})
@EnableAutoConfiguration
public class MvcConfiguration implements WebMvcConfigurer, ApplicationContextAware {
    private static final Logger logger = LogManager.getLogger(MvcConfiguration.class);
    private WebApplicationContext applicationContext;

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver ret = new InternalResourceViewResolver();
        ret.setViewClass(JstlView.class);
        ret.setSuffix(".jsp");
        ret.setPrefix("/view/");
        return ret;
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(ApplicationSettings settings) {
        logger.debug("Create a web server factory customized");
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                logger.debug("The customized for " + factory);
                if (factory instanceof TomcatServletWebServerFactory) {
                    TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) factory;
                    if (!StringUtils.isEmpty(settings.getDocRoot())) {
                        File fDir = new File(settings.getDocRoot());
                        tomcat.setDocumentRoot(fDir);
                        logger.debug("Set document root to " + fDir.getPath());
                    }
                    if (!StringUtils.isEmpty(settings.getBaseDir())) {
                        File root = new File(settings.getBaseDir());
                        tomcat.setBaseDirectory(root);
                        logger.debug("Set base dir to " + root.getPath());
                    }
                    //tomcat.addContextCustomizers(new Customizer());
                    Jsp jspConfig = new Jsp();
                    Map<String, String> params = new HashMap<>();
                    params.put("checkInterval", "0");
                    jspConfig.setInitParameters(params);
                    tomcat.setJsp(jspConfig);
                }
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (WebApplicationContext) applicationContext;
    }

}
