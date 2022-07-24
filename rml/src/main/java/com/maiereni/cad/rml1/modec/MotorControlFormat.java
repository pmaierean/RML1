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
import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.bo.IntArgument;

import java.util.List;

/**
 * When parameter n is other than 0 or when the parameter is omitted, the
 * spindle motor is set to a rotatable state.
 * What actually rotates is .
 * Thereafter, when the tool is moved by a command, the spindle motor is
 * made to rotate from a stopped state, and then the movement is performed.
 * When n is 0, rotation thereafter is prohibited.
 * Also, rotation is stopped immediately after the motor stops.
 * This command is effective in performing rotation or stoppage after waiting
 * until the motor actually stops.
 *
 * @author Petre Maierean
 */
public class MotorControlFormat extends RML1CommandFormat {
    public static final String NAME = "Motor control";
    public static final String PATTERN = "!MC(" + INT_PATTERN + ")?;";

    /**
     * Generate command
     *
     * @param arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, IntArgument.class, 0);
        String ret = null;
        if (arguments == null || arguments.size() == 0) {
            ret = "!MC;";
        } else {
            ret = String.format("!MC %s;", arguments.get(0).toString());
        }
        return ret;
    }


    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        if (arguments.size() == 0) {
            try {
                String s = token;
                if (s.startsWith("!MC")) {
                    s = s.substring(3);
                }
                if (s.endsWith(";")) {
                    s = s.substring(0, s.length() - 1);
                }
                ret = argumentParser.parse(s, IntArgument.class);
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }
}
