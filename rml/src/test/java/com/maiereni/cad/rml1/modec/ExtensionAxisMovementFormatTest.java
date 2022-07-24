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
package com.maiereni.cad.rml1.modec;

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.AxisArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class ExtensionAxisMovementFormatTest {
    private static final Logger logger = LogManager.getLogger(ExtensionAxisMovementFormatTest.class);
    private ExtensionAxisMovementFormat format = new ExtensionAxisMovementFormat();

    @Test
    public void testInvalidNull() {
        try {
            format.generateCommand(null);
            format.generateCommand(null);
            fail("Unexpected result for empty arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmpty() {
        try {
            String s = format.generateCommand(new ArrayList<>());
            fail("Unexpected result for empty arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testOneArgumentX() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new AxisArgument(1f, null, null, null));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZE X1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testOneArgumentY() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new AxisArgument(null, 1f, null, null));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZE Y1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testOneArgumentZ() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new AxisArgument(null, null, 1f, null));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZE Z1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testOneArgumentA() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new AxisArgument(null, null, null, 12.3f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZE A12.3;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testArgument() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new AxisArgument(1f, 2f, 3f, 4f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZE X1Y2Z3A4;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new AxisArgument(1f, 2f, 3f, null));
            arguments.add(new AxisArgument(null, null, null, 4f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZE X1Y2Z3:A4;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseNull() {
        try {
            format.parseArguments(null);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseBlank() {
        try {
            format.parseArguments("");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseInvalid() {
        try {
            format.parseArguments("abc;");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseAxisA() {
        String pattern = ExtensionAxisMovementFormat.PATTERN;
        logger.debug("Pattern {}", pattern);
        if ("!ZE A123;".matches(pattern)) {
            logger.debug("matches");
        }
        try {
            List<CommandArgument> arguments = format.parseArguments("!ZE A123;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof AxisArgument);
            AxisArgument argument = (AxisArgument) arguments.get(0);
            assertTrue(argument.getA() == 123f);
            assertNull(argument.getX());
            assertNull(argument.getY());
            assertNull(argument.getZ());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseAxisX() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!ZE X123.2;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof AxisArgument);
            AxisArgument argument = (AxisArgument) arguments.get(0);
            assertTrue(argument.getX() == 123.2f);
            assertNull(argument.getA());
            assertNull(argument.getY());
            assertNull(argument.getZ());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseAxisY() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!ZE Y123.2;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof AxisArgument);
            AxisArgument argument = (AxisArgument) arguments.get(0);
            assertTrue(argument.getY() == 123.2f);
            assertNull(argument.getA());
            assertNull(argument.getX());
            assertNull(argument.getZ());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseAxisZ() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!ZE Z123.2;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof AxisArgument);
            AxisArgument argument = (AxisArgument) arguments.get(0);
            assertTrue(argument.getZ() == 123.2f);
            assertNull(argument.getA());
            assertNull(argument.getY());
            assertNull(argument.getX());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseAxis() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!ZE X234.5,Z123.2;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof AxisArgument);
            AxisArgument argument = (AxisArgument) arguments.get(0);
            assertTrue(argument.getX() == 234.5f);
            assertTrue(argument.getZ() == 123.2f);
            assertNull(argument.getA());
            assertNull(argument.getY());
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
