/**
 * ================================================================
 * Copyright (c) 2020-2021 Maiereni Software and Consulting Inc
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
import com.maiereni.cad.rml1.bo.FloatArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class HomeMovementFormatTest {
    private static final Logger logger = LogManager.getLogger(HomeMovementFormatTest.class);
    private HomeMovementFormat format = new HomeMovementFormat();

    @Test
    public void testNull() {
        try {
            String s = format.generateCommand(null);
            assertEquals("Expected format", "H;", s);
        } catch (Exception e) {
            logger.error("Failed to generate command", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testBlank() {
        try {
            String s = format.generateCommand(new ArrayList<>());
            assertEquals("Expected format", "H;", s);
        } catch (Exception e) {
            logger.error("Failed to generate command", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testAny() {
        try {
            List<CommandArgument> args = new ArrayList<>();
            args.add(new FloatArgument(123.4f));
            String s = format.generateCommand(args);
            assertEquals("Expected format", "H;", s);
        } catch (Exception e) {
            logger.error("Failed to generate command", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseSimple() {
        try {
            List<CommandArgument> commands = format.parseArguments("H;");
            assertNotNull(commands);
            assertTrue(commands.size() == 0);
        } catch (Exception e) {
            logger.error("Failed to parse string", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseWrongParameters() {
        try {
            format.parseArguments("H123;");
            fail("Unexpected result for empty arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseWrongString() {
        try {
            format.parseArguments("abc");
            fail("Unexpected result for empty arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseWrongNull() {
        try {
            format.parseArguments(null);
            fail("Unexpected result for empty arguments");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
