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
package com.maiereni.cad.rml1.mode1;

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class ThreeAxisFormatTest {
    private static final Logger logger = LogManager.getLogger(ThreeAxisFormatTest.class);
    private ThreeAxisFormat format = new ThreeAxisFormat();

    @Test
    public void testInvalidNull() {
        try {
            format.generateCommand(null);
            fail("Unexpected result for null arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmpty() {
        try {
            format.generateCommand(new ArrayList<>());
            fail("Unexpected result for null arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testOneArgument() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new VertexArgument(123.1f, 456.1f, 789.1f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "Z 123.1,456.1,789.1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testTwoArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new VertexArgument(123.1f, 456.1f, 789.1f));
            arguments.add(new VertexArgument(789.1f, 123.1f, 456.1f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "Z 123.1,456.1,789.1,789.1,123.1,456.1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseBad() {
        assertTrue(!format.isParseable(null));
        assertTrue(!format.isParseable("abc"));
        assertTrue(!format.isParseable("Z"));
        assertTrue(!format.isParseable("Z123;"));
        assertTrue(!format.isParseable("Z123.1;"));
        assertTrue(!format.isParseable("Z123.1,234.2;"));
    }

    @Test
    public void testParseSimple() {
        try {
            List<CommandArgument> arguments = format.parseArguments("Z123.0,234.1,345.5;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof VertexArgument);
            VertexArgument argument = (VertexArgument) arguments.get(0);
            assertTrue(argument.getX() == 123.0f);
            assertTrue(argument.getY() == 234.1f);
            assertTrue(argument.getZ() == 345.5f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseWithSpaceSimple() {
        try {
            List<CommandArgument> arguments = format.parseArguments("Z 123.0, 234.1, 345.5;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof VertexArgument);
            VertexArgument argument = (VertexArgument) arguments.get(0);
            assertTrue(argument.getX() == 123.0f);
            assertTrue(argument.getY() == 234.1f);
            assertTrue(argument.getZ() == 345.5f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseTwo() {
        try {
            List<CommandArgument> arguments = format.parseArguments("Z123.0,234.1,345.5,32.1,43.2,54.3;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 2);
            assertTrue(arguments.get(0) instanceof VertexArgument);
            VertexArgument argument = (VertexArgument) arguments.get(0);
            assertTrue(argument.getX() == 123.0f);
            assertTrue(argument.getY() == 234.1f);
            assertTrue(argument.getZ() == 345.5f);
            assertTrue(arguments.get(1) instanceof VertexArgument);
            argument = (VertexArgument) arguments.get(1);
            assertTrue(argument.getX() == 32.1f);
            assertTrue(argument.getY() == 43.2f);
            assertTrue(argument.getZ() == 54.3f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseTwoWithSpaces() {
        try {
            List<CommandArgument> arguments = format.parseArguments("Z 123.0, 234.1, 345.5, 32.1, 43.2, 54.3;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 2);
            assertTrue(arguments.get(0) instanceof VertexArgument);
            VertexArgument argument = (VertexArgument) arguments.get(0);
            assertTrue(argument.getX() == 123.0f);
            assertTrue(argument.getY() == 234.1f);
            assertTrue(argument.getZ() == 345.5f);
            assertTrue(arguments.get(1) instanceof VertexArgument);
            argument = (VertexArgument) arguments.get(1);
            assertTrue(argument.getX() == 32.1f);
            assertTrue(argument.getY() == 43.2f);
            assertTrue(argument.getZ() == 54.3f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseOne() {
        try {
            List<CommandArgument> arguments = format.parseArguments("Z0,0,0;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof VertexArgument);
            VertexArgument argument = (VertexArgument) arguments.get(0);
            assertTrue(argument.getX() == 0f);
            assertTrue(argument.getY() == 0f);
            assertTrue(argument.getZ() == 0f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseFloatsLong() {
        String s = "Z0.000,400.000,0.000;";
        try {
            List<CommandArgument> arguments = format.parseArguments("Z0.000,400.000,0.000;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof VertexArgument);
            VertexArgument argument = (VertexArgument) arguments.get(0);
            assertTrue(argument.getX() == 0f);
            assertTrue(argument.getY() == 400.0f);
            assertTrue(argument.getZ() == 0f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

}
