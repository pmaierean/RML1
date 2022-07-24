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
package com.maiereni.cad.rml1.mode2;

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.IntArgument;
import com.maiereni.cad.rml1.bo.LongArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class VelocitySelectionFormatTest {
    private static final Logger logger = LogManager.getLogger(VelocitySelectionFormatTest.class);
    private VelocitySelectionFormat format = new VelocitySelectionFormat();

    @Test
    public void testInvalidNull() {
        try {
            format.generateCommand(null);
            String s = format.generateCommand(new ArrayList<>());
            assertEquals("Expected format", "VS;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testEmpty() {
        try {
            String s = format.generateCommand(new ArrayList<>());
            assertEquals("Expected format", "VS;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testLongArgument() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new LongArgument(123L));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "VS 123;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testFloatArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new FloatArgument(123.1f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "VS 123.1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseSimple() {
        try {
            List<CommandArgument> arguments = format.parseArguments("VS;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 0);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseWithFloat() {
        try {
            List<CommandArgument> arguments = format.parseArguments("VS 123.4;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof FloatArgument);
            FloatArgument fa = (FloatArgument) arguments.get(0);
            assertTrue(fa.getF() == 123.4f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseWithFloatSpace() {
        logger.debug("Pattern is: {}", VelocitySelectionFormat.PATTERN);
        Pattern p = Pattern.compile(VelocitySelectionFormat.PATTERN);
        Matcher m = p.matcher("VS 123.4;");
        try {
            List<CommandArgument> arguments = format.parseArguments("VS 123.4;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof FloatArgument);
            FloatArgument fa = (FloatArgument) arguments.get(0);
            assertTrue(fa.getF() == 123.4f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseWithFloatSpaces() {
        try {
            List<CommandArgument> arguments = format.parseArguments("VS 123.4 ;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof FloatArgument);
            FloatArgument fa = (FloatArgument) arguments.get(0);
            assertTrue(fa.getF() == 123.4f);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseInt() {
        try {
            List<CommandArgument> arguments = format.parseArguments("VS123;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) arguments.get(0);
            assertTrue(i.getValue() == 123);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseIntWithSpace() {
        try {
            List<CommandArgument> arguments = format.parseArguments("VS 123;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) arguments.get(0);
            assertTrue(i.getValue() == 123);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }
}
