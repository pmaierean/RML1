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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for the DefaultSettingFormat class
 *
 * @author Petre Maierean
 */
public class DefaultSettingsFormatTest {
    private DefaultSettingsFormat format = new DefaultSettingsFormat();

    @Test
    public void testFormatNull() {
        try {
            String cmd = format.generateCommand(null);
            assertEquals("Expected command", "DF;", cmd);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFormatEmpty() {
        try {
            String cmd = format.generateCommand(new ArrayList<>());
            assertEquals("Expected command", "DF;", cmd);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testFormatInvalid() {
        try {
            List<CommandArgument> args = new ArrayList<>();
            args.add(new FloatArgument(123.4f));
            String cmd = format.generateCommand(args);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseNull() {
        try {
            List<CommandArgument> arguments = format.parseArguments(null);
            assertNotNull(arguments);
            assertTrue(arguments.size() == 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testParseBlank() {
        try {
            List<CommandArgument> arguments = format.parseArguments("");
            assertNotNull(arguments);
            assertTrue(arguments.size() == 0);
        } catch (Exception e) {
            fail();
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

}
