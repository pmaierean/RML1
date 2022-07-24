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
package com.maiereni.cad.rmlserver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Clones resources such as JSP, JS and CSS files to local temporary folder
 *
 * @author Petre Maierean
 */
public class WebResourcesUtil {
    private static final Logger logger = LogManager.getLogger(WebResourcesUtil.class);

    /**
     * Copies internal resources to the destination folder. If the argument is null then creates a new temporary directory
     *
     * @param fDocs
     * @return the directory containing the Web resources
     * @throws Exception
     */
    public File copyViewResources(File fDocs) throws Exception {
        File ret = null;
        File dest = fDocs;
        if (dest == null || !dest.isDirectory()) {
            dest = Files.createTempDirectory("rml").toFile();
        }
        URI uri = RmlserverApplication.class.getResource("/static").toURI();
        if (uri.getScheme().equals("file")) {
            ret = new File(uri.toString().substring(6));
        } else if (uri.getScheme().equals("jar")) {
            int ix = uri.toString().indexOf("!");
            String path = uri.toString().substring(ix + 2).replace("!", "");
            logger.debug("Copy all content from " + path);
            copyViewResources(dest, path);
            ret = new File(dest, path);
        }
        return ret;
    }

    private void copyViewResources(File fDocs, String path) throws Exception {
        CodeSource src = RmlserverApplication.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            try (InputStream is = src.getLocation().openStream();
                 ZipInputStream zip = new ZipInputStream(is);) {
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith(path)) {
                        File f = new File(fDocs, name);
                        if (e.isDirectory()) {
                            if (!f.isDirectory())
                                if (!f.mkdirs()) {
                                    throw new Exception("Cannot make directory");
                                }
                        } else {
                            byte[] buffer = readCurrentEntry(zip);
                            FileUtils.writeByteArrayToFile(f, buffer);
                        }
                    }
                }
            }
        } else {
            logger.error("No source");
        }
    }

    private byte[] readCurrentEntry(ZipInputStream zip) throws Exception {
        return IOUtils.toByteArray(zip);
    }

    private String makeDirs(File fDir) throws Exception {
        if (!fDir.isDirectory())
            if (!fDir.mkdirs())
                throw new Exception("Cannot make directory at " + fDir.getPath());
        return fDir.getPath();
    }

}
