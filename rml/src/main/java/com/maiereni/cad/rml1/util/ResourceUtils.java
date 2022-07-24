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
package com.maiereni.cad.rml1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

/**
 * A simple utility class that reads various configuration Pojos from resource files
 *
 * @author Petre Maierean
 */
public class ResourceUtils {
    private static final Logger logger = LogManager.getLogger(ResourceHolder.class);
    private static final Map<String, ResourceHolder> cache = new Hashtable<>();

    /**
     * Get a resource object of type T from local resources
     *
     * @param path   the class path to the resource
     * @param locale the locale
     * @param clazz  the class type
     * @param <T>
     * @return
     * @throws Exception
     */
    public static synchronized <T> T getResources(String path, Locale locale, Class<T> clazz) throws Exception {
        String actualPath = computePath(path, locale);
        T ret = null;
        if (isCached(path, locale, clazz)) {
            ret = cache.get(actualPath).getResource(clazz);
        } else {
            ObjectMapper objectMapper = new ObjectMapper((new YAMLFactory()));
            try (InputStream is = ResourceHolder.class.getResourceAsStream(actualPath)) {
                if (is == null) {
                    throw new Exception("Cannot resolve path " + actualPath);
                }
                ret = objectMapper.readValue(is, clazz);
                if (ret != null) {
                    ResourceHolder holder = new ResourceHolder(ret);
                    cache.put(actualPath, holder);
                }
            }
        }
        return ret;
    }

    /**
     * Tests if the configuration file at path and locale has been cached and the object is of type T
     *
     * @param path
     * @param locale
     * @param clazz
     * @param <T>
     * @return
     */
    public static synchronized <T> boolean isCached(String path, Locale locale, Class<T> clazz) {
        boolean ret = false;
        String actualPath = computePath(path, locale);
        if (cache.containsKey(actualPath) && clazz.isInstance(cache.get(actualPath).resource)) {
            ret = true;
        }
        return ret;
    }

    private static String computePath(String path, Locale locale) {
        String ret = path;
        if (locale != null) {
            ret = path + "_" + locale.getLanguage();
        } else {
            ret = path + "_en";
        }
        ret += ".yml";
        return ret;
    }

    private static class ResourceHolder {
        private Object resource;

        public ResourceHolder(Object resource) {
            this.resource = resource;
        }

        public <T> T getResource(Class<T> clazz) throws Exception {
            if (resource != null && clazz.isInstance(resource)) {
                return (T) resource;
            }
            throw new Exception("Invalid resource");
        }
    }
}
