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
import com.maiereni.cad.rml1.bo.LongArgument;

import java.util.List;

/**
 * This sets the rotating speed of the spindle motor.
 * Operation varies according to the value of the parameter.
 * Note that models that are not equipped with both are not provided with a
 * method for specifying direct rpm.
 * When at 0 to 99
 * The setting range for spindle speed is divided into 16 stages from 0 to 15.
 * These are not absolute values.
 * A value of 0 is the lowest spindle speed, and 15 is the maximum speed.
 * Values higher than 15 are clipped to 15.
 * When at 100 to 8388607
 * Models that can specify the spindle speed as a direct rpm value specify
 * the value directly.
 * However, the actual effective number of digits is model-dependent.
 * For example, a value of 3124 may produce such differing results as 3,000
 * rpm, 3,100 rpm, 3,120 rpm, or 3,124 rpm, depending on the model.
 * Digits other than effective digits are discarded.
 * Also, when a spindle speed exceeding the model's capacity is specified,
 * the model's maximum speed is used. Similarly, when a spindle speed
 * below the model's capacity is specified, the model's minimum speed is
 * used.
 * On models not capable of specifying direct rpm, the value is clipped at 15
 * and the maximum spindle speed is used.
 * The setting is enabled after operation of the X, Y, and Z motors stops.
 * When there is no parameter, no execution is performed. The command is
 * ignored.
 * Depending on the model, it may not be possible to manipulate the spindle
 * rotating speed using commands. In such cases, the command, including
 * the parameter, is ignored.
 *
 * @author Petre Maierean
 */
public class RevolutionControlFormat extends RML1CommandFormat {
    public static final String NAME = "Revolution Control";
    public static final String PATTERN = "!RC(" + INT_PATTERN + ")?;";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, LongArgument.class, 0);
        String ret = null;
        if (arguments == null || arguments.size() == 0) {
            ret = "!RC;";
        } else {
            ret = String.format("!RC %s;", arguments.get(0).toString());
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        logger.debug("Token {}='{}'", i, token);
        if (arguments.size() == 0) {
            try {
                String s = token;
                if (token.startsWith("!RC")) {
                    s = s.substring(3);
                }
                if (token.endsWith(";")) {
                    s = s.substring(0, s.length() - 1);
                }
                ret = argumentParser.parse(s, LongArgument.class);
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }
}
