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

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.fail;

/**
 * @author Petre Maierean
 */
public class TranslatorTest {
    private static final Logger logger = LogManager.getLogger(TranslatorTest.class);
    private static final String SAMPLE1 = "^DF;!MC1;!PZ0,0;V15.0;Z0,0,0;!MC0;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
    private static final String SAMPLE2 = "^DF;!MC0;!PZ0,0;V15.0;Z0.000,400.000,0.000;!MC0;";
    private Translator translator = new Translator();

    @Test
    public void testSample1() {
        try {
            List<String> arr = translator.getTranslation(SAMPLE1, Locale.ENGLISH);
            StringWriter sw = new StringWriter();
            for (String s : arr) {
                sw.write(s);
                sw.write("\r\n");
            }
            logger.debug("Text translation for {} :\r\n{}", SAMPLE1, sw.toString());
        } catch (Exception e) {
            logger.error("Failed to translate", e);
            fail();
        }
    }

    @Test
    public void testSample2() {
        try {
            List<String> arr = translator.getTranslation(SAMPLE2, null);
            StringWriter sw = new StringWriter();
            for (String s : arr) {
                sw.write(s);
                sw.write("\r\n");
            }
            logger.debug("Text translation for {} :\r\n{}", SAMPLE2, sw.toString());
        } catch (Exception e) {
            logger.error("Failed to translate", e);
            fail();
        }
    }

    /**
     * Translate a text file into a readable command
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                Translator translator = new Translator();
                String sp = translator.translate(new File(args[0]), null);
                if (args.length > 0) {
                    File fout = new File(args[1]);
                    FileUtils.write(fout, sp, StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                logger.error("Failed to convert", e);
            }
        }
    }
}
