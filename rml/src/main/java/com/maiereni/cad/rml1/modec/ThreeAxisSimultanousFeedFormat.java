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

import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;

import java.io.StringWriter;
import java.util.List;

/**
 * This moves simultaneously along the three axes from the present
 * coordinates to the specified coordinate values.
 * The speed is as set by the V or !VZ command.
 * When no parameter is present, no operation is performed.
 * Additionally, the parameters are interpreted as absolute coordinates when
 * set to absolute coordinates prior to this command and are interpreted as
 * relative values when set to relative coordinates prior to this command.
 *
 * @author Petre Maierean
 */
public class ThreeAxisSimultanousFeedFormat extends RML1CommandFormat {
    public static final String NAME = "Three axis simultaneous feed";
    public static final String PATTERN = "!ZZ(" + THREE_FLOATS_PATTERN + "(\\x2c" + THREE_FLOATS_PATTERN + ")*);";

    /**
     * Generates command
     *
     * @param arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, VertexArgument.class, 1);
        StringWriter sw = new StringWriter();
        sw.write("!ZZ ");
        boolean hasComma = false;
        for (CommandArgument argument : arguments) {
            if (hasComma) {
                sw.write(",");
            }
            VertexArgument vertexArgument = (VertexArgument) argument;
            sw.write(vertexArgument.toString());
            hasComma = true;
        }
        sw.write(COMMAND_TEMINATOR);
        return sw.toString();
    }


    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        logger.debug("process {} at {}", token, i);
        try {
            String s = token;
            if (s.startsWith(",")) {
                s = s.substring(1);
            }
            if (s.contains(",")) {
                s = trimString(s, 2);
                ret = argumentParser.parse(s, VertexArgument.class);
            }
        } catch (Exception e) {
            logger.error("Failure to parse token '{}' at position {}", token, i, e);
        }
        return ret;
    }

    @Override
    protected String getSpecialCharactersToAvoid() {
        return "!ZZ.*;";
    }
}
