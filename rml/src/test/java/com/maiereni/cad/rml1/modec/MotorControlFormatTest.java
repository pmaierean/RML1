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
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.IntArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class MotorControlFormatTest {
    private static final Logger logger = LogManager.getLogger(MotorControlFormatTest.class);
    private MotorControlFormat format = new MotorControlFormat();

    @Test
    public void testFormatNull() {
        try {
            String cmd = format.generateCommand(null);
            assertEquals("Expected command", "!MC;", cmd);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFormatEmpty() {
        try {
            String cmd = format.generateCommand(new ArrayList<>());
            assertEquals("Expected command", "!MC;", cmd);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFormatInvalid() {
        try {
            List<CommandArgument> args = new ArrayList<>();
            args.add(new FloatArgument(123));
            String cmd = format.generateCommand(args);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFormatValid() {
        try {
            List<CommandArgument> args = new ArrayList<>();
            args.add(new IntArgument(123));
            String cmd = format.generateCommand(args);
            assertEquals("Expected value", "!MC 123;", cmd);
        } catch (Exception e) {
            logger.error("Failed to process valid generation", e);
            fail();
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
    public void testParseNotBlank() {
        try {
            format.parseArguments("1234.5");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseBlankCommand() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!MC;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 0);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }


    @Test
    public void testParseInt() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!MC123;");
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
            List<CommandArgument> arguments = format.parseArguments("!MC 123;");
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
