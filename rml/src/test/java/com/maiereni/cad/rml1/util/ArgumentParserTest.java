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

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A unit test for the ArgumentParser
 *
 * @author Petre Maierean
 */
public class ArgumentParserTest {
    private static final Logger logger = LogManager.getLogger(ArgumentParserTest.class);
    private ArgumentParser argumentParser = new ArgumentParser();

    @Test
    public void testNullText() {
        try {
            argumentParser.parse(null, FloatArgument.class);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNullClass() {
        try {
            argumentParser.parse("Some text", null);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseVertex() {
        try {
            CommandArgument argument = argumentParser.parse("123.4,234.5,345.6", VertexArgument.class);
            assertNotNull(argument);
            assertTrue(argument instanceof VertexArgument);
            VertexArgument vertex = (VertexArgument) argument;
            assertEquals("Expected value", "123.4,234.5,345.6", vertex.toString());
        } catch (Exception e) {
            logger.error("Failed to parse a vertex string", e);
            fail();
        }
    }
}
