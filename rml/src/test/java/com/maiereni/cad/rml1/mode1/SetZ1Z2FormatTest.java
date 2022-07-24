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
import com.maiereni.cad.rml1.bo.IntArgument;
import com.maiereni.cad.rml1.bo.LongArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class SetZ1Z2FormatTest {
    private static final Logger logger = LogManager.getLogger(SetZ1Z2FormatTest.class);

    @Test
    public void testInvalidNull() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            format.generateCommand(null);
            fail("Unexpected result for null arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInvalidEmpty() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            format.generateCommand(new ArrayList<>());
            fail("Unexpected result for empty arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testOneArgument() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new LongArgument(123L));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "@ 123;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testTwoArguments() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new LongArgument(123L));
            arguments.add(new LongArgument(456L));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "@ 123,456;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseSimple() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            format.parseArguments("@;");
            fail("Unexpected behavior");
        } catch (Exception e) {
            assertTrue("Expected", true);
        }
    }

    @Test
    public void testParseOneArgument() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            List<CommandArgument> commandArguments = format.parseArguments("@123;");
            assertNotNull(commandArguments);
            assertTrue(commandArguments.size() == 1);
            assertTrue(commandArguments.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) commandArguments.get(0);
            assertTrue(i.getValue() == 123);
        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail("Unexpected behavior");
        }
    }

    @Test
    public void testParseTwoArguments() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            List<CommandArgument> commandArguments = format.parseArguments("@ 123,345;");
            assertNotNull(commandArguments);
            assertTrue(commandArguments.size() == 2);
            assertTrue(commandArguments.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) commandArguments.get(0);
            assertTrue(i.getValue() == 123);

            assertTrue(commandArguments.get(1) instanceof IntArgument);
            i = (IntArgument) commandArguments.get(1);
            assertTrue(i.getValue() == 345);

        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail("Unexpected behavior");
        }
    }

    @Test
    public void testParseTwoArgumentsSpace() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            List<CommandArgument> commandArguments = format.parseArguments("@ 123, 345;");
            assertNotNull(commandArguments);
            assertTrue(commandArguments.size() == 2);
            assertTrue(commandArguments.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) commandArguments.get(0);
            assertTrue(i.getValue() == 123);

            assertTrue(commandArguments.get(1) instanceof IntArgument);
            i = (IntArgument) commandArguments.get(1);
            assertTrue(i.getValue() == 345);

        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail("Unexpected behavior");
        }
    }

    @Test
    public void testParseTooManyArguments() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            format.parseArguments("@ 123, 456, 789;");
            fail("Unexpected behavior");
        } catch (Exception e) {
            assertTrue("Expected", true);
        }
    }

    @Test
    public void testParseWithZerro() {
        try {
            SetZ1Z2Format format = new SetZ1Z2Format();
            List<CommandArgument> commandArguments = format.parseArguments("@ -2000,0;");
            assertNotNull(commandArguments);
            assertTrue(commandArguments.size() == 2);
            assertTrue(commandArguments.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) commandArguments.get(0);
            assertTrue(i.getValue() == -2000);

            assertTrue(commandArguments.get(1) instanceof IntArgument);
            i = (IntArgument) commandArguments.get(1);
            assertTrue(i.getValue() == 0);

        } catch (Exception e) {
            logger.error("Failed to parse", e);
            fail("Unexpected behavior");
        }
    }

}
