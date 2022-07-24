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
import com.maiereni.cad.rml1.bo.PairOfFloatsArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class RelativeDrawLineCuttingFormatTest {
    private static final Logger logger = LogManager.getLogger(RelativeDrawLineCuttingFormatTest.class);
    private RelativeDrawLineCuttingFormat format = new RelativeDrawLineCuttingFormat();

    @Test
    public void testInvalidNull() {
        try {
            String s = format.generateCommand(null);
            assertEquals("Expected format", "I;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testEmpty() {
        try {
            String s = format.generateCommand(new ArrayList<>());
            assertEquals("Expected format", "I;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testOneArgument() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new PairOfFloatsArgument(123.1f, 456.1f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "I 123.1,456.1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testTwoArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new PairOfFloatsArgument(123.1f, 456.1f));
            arguments.add(new PairOfFloatsArgument(789.1f, 123.1f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "I 123.1,456.1,789.1,123.1;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
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
            List<CommandArgument> args = format.parseArguments("I;");
            assertNotNull(args);
            assertTrue(args.size() == 0);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }

    @Test
    public void testParseArgumentsWithSpace() {
        try {
            List<CommandArgument> args = format.parseArguments("I 123.5,234.5;");
            assertNotNull(args);
            assertTrue(args.size() == 1);
            assertTrue(args.get(0) instanceof PairOfFloatsArgument);
            PairOfFloatsArgument a = (PairOfFloatsArgument) args.get(0);
            assertTrue(a.getX() == 123.5f);
            assertTrue(a.getY() == 234.5f);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }

    @Test
    public void testParseArgumentsMultipleWithSpace() {
        try {
            List<CommandArgument> args = format.parseArguments("I 123.5,234.5,67.2,21.1;");
            assertNotNull(args);
            assertTrue(args.size() == 2);
            assertTrue(args.get(0) instanceof PairOfFloatsArgument);
            PairOfFloatsArgument a = (PairOfFloatsArgument) args.get(0);
            assertTrue(a.getX() == 123.5f);
            assertTrue(a.getY() == 234.5f);
            assertTrue(args.get(1) instanceof PairOfFloatsArgument);
            a = (PairOfFloatsArgument) args.get(1);
            assertTrue(a.getX() == 67.2f);
            assertTrue(a.getY() == 21.1f);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail();
        }
    }
}
