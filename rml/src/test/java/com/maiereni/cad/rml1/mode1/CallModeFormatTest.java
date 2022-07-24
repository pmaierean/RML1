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
import com.maiereni.cad.rml1.bo.CallArgument;
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.Mode2Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class CallModeFormatTest {
    private static final Logger logger = LogManager.getLogger(CallModeFormatTest.class);
    private CallModeFormat format = new CallModeFormat();

    @Test
    public void testBlank() {
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
    public void testWrong() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            CallArgument callArgument = new CallArgument();
            arguments.add(callArgument);
            format.generateCommand(arguments);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGoodNoArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            CallArgument callArgument = new CallArgument();
            callArgument.setMode2Command(Mode2Command.initialize);
            arguments.add(callArgument);
            String s = format.generateCommand(arguments);
            assertEquals("Expected command", "^ IN;", s);
        } catch (Exception e) {
            logger.error("Failed to create command", e);
            fail();
        }
    }

    @Test
    public void testGoodOneArguments() {
        try {
            List<CommandArgument> arguments = new ArrayList<>();
            CallArgument callArgument = new CallArgument();
            callArgument.setMode2Command(Mode2Command.velocitySelection);
            arguments.add(callArgument);
            arguments.add(new FloatArgument(125.5f));
            String s = format.generateCommand(arguments);
            assertEquals("Expected command", "^ VS 125.5;", s);
        } catch (Exception e) {
            logger.error("Failed to create command", e);
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
    public void testParseRandom() {
        try {
            format.parseArguments("abd");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseEmpty() {
        try {
            format.parseArguments("^;");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseGood() {
        try {
            List<CommandArgument> arguments = format.parseArguments("^ VS 125.5;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 2);
            assertTrue(arguments.get(0) instanceof CallArgument);
            CallArgument callArgument = (CallArgument) arguments.get(0);
            assertEquals("", Mode2Command.velocitySelection, callArgument.getMode2Command());
            assertTrue(arguments.get(1) instanceof FloatArgument);
            FloatArgument floatArgument = (FloatArgument) arguments.get(1);
            assertTrue(floatArgument.getF() == 125.5f);
        } catch (Exception e) {
            logger.error("Failed to parse a valid string", e);
            fail();
        }
    }

    @Test
    public void testParseGoodNoSpace() {
        try {
            List<CommandArgument> arguments = format.parseArguments("^ VS125.5;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 2);
            assertTrue(arguments.get(0) instanceof CallArgument);
            CallArgument callArgument = (CallArgument) arguments.get(0);
            assertEquals("", Mode2Command.velocitySelection, callArgument.getMode2Command());
            assertTrue(arguments.get(1) instanceof FloatArgument);
            FloatArgument floatArgument = (FloatArgument) arguments.get(1);
            assertTrue(floatArgument.getF() == 125.5f);
        } catch (Exception e) {
            logger.error("Failed to parse a valid string", e);
            fail();
        }
    }


    @Test
    public void testParseDF() {
        try {
            List<CommandArgument> arguments = format.parseArguments("^DF;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof CallArgument);
            CallArgument callArgument = (CallArgument) arguments.get(0);
            assertEquals("", Mode2Command.defaultFormat, callArgument.getMode2Command());
        } catch (Exception e) {
            logger.error("Failed to parse a valid string", e);
            fail();
        }
    }
}
