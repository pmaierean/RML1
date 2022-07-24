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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class DwellFormatTest {
    private static final Logger logger = LogManager.getLogger(DwellFormatTest.class);

    @Test
    public void testInvalidNull() {
        try {
            DwellFormat format = new DwellFormat();
            format.generateCommand(null);
            String s = format.generateCommand(new ArrayList<>());
            assertEquals("Expected format", "W;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testEmpty() {
        try {
            DwellFormat format = new DwellFormat();
            String s = format.generateCommand(new ArrayList<>());
            assertEquals("Expected format", "W;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testOneArgument() {
        try {
            DwellFormat format = new DwellFormat();
            List<CommandArgument> arguments = new ArrayList<>();
            arguments.add(new IntArgument(123));
            String s = format.generateCommand(arguments);
            assertEquals("Expected format", "W 123;", s);
        } catch (Exception e) {
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseSimple() {
        try {
            DwellFormat format = new DwellFormat();
            List<CommandArgument> commands = format.parseArguments("W;");
            assertNotNull(commands);
            assertTrue(commands.size() == 0);
        } catch (Exception e) {
            logger.error("Failed to parse string", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseParameter() {
        try {
            DwellFormat format = new DwellFormat();
            List<CommandArgument> commands = format.parseArguments("W123;");
            assertNotNull(commands);
            assertTrue(commands.size() == 1);
            assertTrue(commands.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) commands.get(0);
            assertTrue(i.getValue() == 123);
        } catch (Exception e) {
            logger.error("Failed to parse string", e);
            fail("Unexpected result for empty arguments");
        }
    }

    @Test
    public void testParseParameterWithSpace() {
        try {
            DwellFormat format = new DwellFormat();
            List<CommandArgument> commands = format.parseArguments("W 123 ;");
            assertNotNull(commands);
            assertTrue(commands.size() == 1);
            assertTrue(commands.get(0) instanceof IntArgument);
            IntArgument i = (IntArgument) commands.get(0);
            assertTrue(i.getValue() == 123);
        } catch (Exception e) {
            logger.error("Failed to parse string", e);
            fail("Unexpected result for empty arguments");
        }
    }
}
