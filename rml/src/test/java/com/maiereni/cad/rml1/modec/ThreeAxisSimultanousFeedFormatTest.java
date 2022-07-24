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
import com.maiereni.cad.rml1.bo.VertexArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class ThreeAxisSimultanousFeedFormatTest {
    private static final Logger logger = LogManager.getLogger(ThreeAxisSimultanousFeedFormatTest.class);
    private ThreeAxisSimultanousFeedFormat format = new ThreeAxisSimultanousFeedFormat();

    @Test
    public void testInvalidNull() {
        try {
            format.generateCommand(null);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmpty() {
        try {
            format.generateCommand(new ArrayList<>());
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testOneArgument() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new VertexArgument(123.1f, 456.1f, 567.2f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZZ 123.1,456.1,567.2;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testTwoArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new VertexArgument(123.1f, 456.1f, 567.2f));
            arguments.add(new VertexArgument(789.1f, 123.1f, 422.1f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "!ZZ 123.1,456.1,567.2,789.1,123.1,422.1;", s);
        } catch (Exception e) {
            logger.error("Failed to generate command with two arguments", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testDescriptionNullLocale() {
        String description = format.describeCommand(null, null);
        assertNotNull(description);
    }

    @Test
    public void testDescription() {
        String description = format.describeCommand(Locale.ENGLISH, null);
        logger.debug("The English description is: '{}'", description);
        assertNotNull(description);
    }

    @Test
    public void testIsParseableNull() {
        assertFalse(format.isParseable(null));
    }

    @Test
    public void testIsParseableNotParseable() {
        assertFalse(format.isParseable("Abc;"));
    }

    @Test
    public void testIsParseableSimple() {
        try {
            format.parseArguments("!ZZ ;");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseArguments() {
        try {
            List<CommandArgument> args = format.parseArguments("!ZZ123.5,234.5,456.7;");
            assertNotNull(args);
            assertTrue(args.size() == 1);
            assertTrue(args.get(0) instanceof VertexArgument);
            VertexArgument a = (VertexArgument) args.get(0);
            assertTrue(a.getX() == 123.5f);
            assertTrue(a.getY() == 234.5f);
            assertTrue(a.getZ() == 456.7f);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }

    @Test
    public void testParseArgumentsWithSpace() {
        try {
            List<CommandArgument> args = format.parseArguments("!ZZ 123.5,234.5,456.7;");
            assertNotNull(args);
            assertTrue(args.size() == 1);
            assertTrue(args.get(0) instanceof VertexArgument);
            VertexArgument a = (VertexArgument) args.get(0);
            assertTrue(a.getX() == 123.5f);
            assertTrue(a.getY() == 234.5f);
            assertTrue(a.getZ() == 456.7f);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }

    @Test
    public void testParseArgumentsMultiple() {
        try {
            List<CommandArgument> args = format.parseArguments("!ZZ123.5,234.5,456.7,67.2,21.1,456.7;");
            assertNotNull(args);
            assertTrue(args.size() == 2);
            assertTrue(args.get(0) instanceof VertexArgument);
            VertexArgument a = (VertexArgument) args.get(0);
            assertTrue(a.getX() == 123.5f);
            assertTrue(a.getY() == 234.5f);
            assertTrue(a.getZ() == 456.7f);
            assertTrue(args.get(1) instanceof VertexArgument);
            a = (VertexArgument) args.get(1);
            assertTrue(a.getX() == 67.2f);
            assertTrue(a.getY() == 21.1f);
            assertTrue(a.getZ() == 456.7f);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }

    @Test
    public void testParseArgumentsMultipleWithSpace() {
        try {
            List<CommandArgument> args = format.parseArguments("!ZZ 123.5,234.5,456.7,67.2,21.1,456.7;");
            assertNotNull(args);
            assertTrue(args.size() == 2);
            assertTrue(args.get(0) instanceof VertexArgument);
            VertexArgument a = (VertexArgument) args.get(0);
            assertTrue(a.getX() == 123.5f);
            assertTrue(a.getY() == 234.5f);
            assertTrue(a.getZ() == 456.7f);
            assertTrue(args.get(1) instanceof VertexArgument);
            a = (VertexArgument) args.get(1);
            assertTrue(a.getX() == 67.2f);
            assertTrue(a.getY() == 21.1f);
            assertTrue(a.getZ() == 456.7f);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }
}
