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
    private SetZ1Z2Format format = new SetZ1Z2Format();

    @Test
    public void testFormatNull() {
        try {
            format.generateCommand(null);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFormatEmpty() {
        try {
            format.generateCommand(new ArrayList<>());
            fail();
        } catch (Exception e) {
            assertTrue(true);
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
            args.add(new LongArgument(123));
            String cmd = format.generateCommand(args);
            assertEquals("Expected value", "!PZ 123;", cmd);
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
            format.parseArguments("!PZ;");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }


    @Test
    public void testParseLong() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!PZ123;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof LongArgument);
            LongArgument f = (LongArgument) arguments.get(0);
            assertTrue(f.getValue() == 123l);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseLongWithSpace() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!PZ 123;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 1);
            assertTrue(arguments.get(0) instanceof LongArgument);
            LongArgument f = (LongArgument) arguments.get(0);
            assertTrue(f.getValue() == 123l);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }

    @Test
    public void testParseTwoLongsWithSpace() {
        try {
            List<CommandArgument> arguments = format.parseArguments("!PZ 123, 345;");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 2);
            assertTrue(arguments.get(0) instanceof LongArgument);
            LongArgument f = (LongArgument) arguments.get(0);
            assertTrue(f.getValue() == 123l);
            assertTrue(arguments.get(1) instanceof LongArgument);
            f = (LongArgument) arguments.get(1);
            assertTrue(f.getValue() == 345l);
        } catch (Exception e) {
            logger.error("Failed to parse simple", e);
            fail();
        }
    }
}
