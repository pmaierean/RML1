/**
 * ================================================================
 * Copyright (c) 2020-2022 Maiereni Software and Consulting Inc
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
package com.maiereni.cad.rmlserver;

import com.maiereni.cad.rmlserver.bo.ApplicationSettings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContextListener;
import java.io.File;
import java.nio.file.Files;

@SpringBootApplication
public class RmlserverApplication extends SpringBootServletInitializer implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(RmlserverApplication.class);
    private static ApplicationSettings APPLICATION_SETTINGS;

    @Bean
    public ApplicationSettings getSettings() throws Exception {
        if (APPLICATION_SETTINGS == null) {
            ApplicationSettings ret = new ApplicationSettings();
            String tmpDir = Files.createTempDirectory("rml").toFile().getAbsolutePath();
            File fDoc = new WebResourcesUtil().copyViewResources(getDir(tmpDir, "docs"));
            ret.setDocRoot(fDoc.getPath());
            ret.setBaseDir(getDir(tmpDir, "base").toString());
            APPLICATION_SETTINGS = ret;
        }
        return APPLICATION_SETTINGS;
    }

    public static void main(String[] args) {
        String logConfigFile = System.getProperty("logging.config");
        if (StringUtils.isNotBlank(logConfigFile)) {
            Configurator.initialize("RML1", logConfigFile);
        } else {
            logConfigFile = RmlserverApplication.class.getResource("/log4j2.xml").getPath();
        }
        Logger logger = LogManager.getLogger(RmlserverApplication.class);
        logger.debug("Log configured from " + logConfigFile);
        final LifecycleManager manager = new LifecycleManager();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                manager.doShutdown();
            }
        }));
    }

    private File getDir(String tmpDir, String subpath) throws Exception {
        File fDir = new File(tmpDir, subpath);
        if (!fDir.mkdirs()) {
            throw new Exception("Could not create folder " + fDir.getPath());
        }
        return fDir;
    }

    private static class ShutdownCleanup implements Runnable {
        @Override
        public void run() {
            if (APPLICATION_SETTINGS != null) {
                File fDir = new File(APPLICATION_SETTINGS.getBaseDir()).getParentFile();
                try {
                    logger.debug("Cleanup temporary folder");
                    FileUtils.deleteDirectory(fDir);
                } catch (Exception e) {
                    logger.error("Failed to clean up directory " + fDir.getPath(), e);
                }
            }
        }
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RmlserverApplication.class);
    }

    public static class LifecycleManager implements Runnable {
        public ConfigurableApplicationContext ctxt;

        LifecycleManager() {
            // as per https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-application-exit
            try {
                ctxt = SpringApplication.run(RmlserverApplication.class);
                Thread th = new Thread(this);
                th.setDaemon(true);
                th.start();
            } catch (Throwable e) {
                logger.error("Failed to create context", e);
            }
        }

        public void doAfterStartup() {
            String[] beanNames = ctxt.getBeanNamesForType(StartupProcessor.class);
            if (beanNames != null) {
                for (String beanName : beanNames) {
                    StartupProcessor processor = ctxt.getBean(beanName, StartupProcessor.class);
                    processor.startup();
                }
            }
        }

        public void doShutdown() {
            logger.debug("Shut down gracefully");
            if (ctxt != null) {
                String[] beanNames = ctxt.getBeanNamesForType(ShutdownProcessor.class);
                if (beanNames != null) {
                    for (String beanName : beanNames) {
                        ShutdownProcessor processor = ctxt.getBean(beanName, ShutdownProcessor.class);
                        processor.preShutdown();
                    }
                }
                SpringApplication.exit(ctxt);
                logger.info("Shutting down!!");
            }
        }

        /**
         * Detects the creation of a file names stop at the user.dir
         * It is interpreted as a signal to close the application after clean up
         */
        @Override
        public void run() {
            String fd = System.getProperty("user.dir");
            File f = new File(fd, "stop");
            while (!f.exists()) {
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    logger.error("Cannot wait", e);
                }
            }
            if (!f.delete()) {
                f.deleteOnExit();
            }
            doShutdown();
        }
    }
}
