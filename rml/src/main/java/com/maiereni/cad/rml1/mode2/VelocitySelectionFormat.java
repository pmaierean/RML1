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
import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.IntArgument;
import com.maiereni.cad.rml1.bo.LongArgument;

import java.util.List;

/**
 * VS without a parameter is set to the default speed.
 * When a parameter is present and is a settable speed, the speed is set to
 * the value of the parameter.
 * When the value of the parameter is the model's capacity or higher, the
 * model's maximum speed is used
 * When the value of the parameter is the model's capacity or lower, the
 * model's minimum speed is used.
 * The given parameter value is not guaranteed to be reached in actual
 * operation.
 * Depending on the length of the line segment, operation may be slower
 * than the value specified by the parameter because of an inability to
 * accelerate fully.
 * Also, even when acceleration is possible, the specified speed may not
 * necessarily be accurately reached; accuracy may be low.
 * Examples for some models:
 * VS 0 may be 0.5 mm/sec.
 * VS 0.1 to VS 1.0 may be in increments of 0.1 mm/sec.
 * Values of VS 1.0 or higher may be in increments of 1 mm/sec.
 * The default value may be, for example, 2 mm/sec.
 *
 * @author Petre Maierean
 */
public class VelocitySelectionFormat extends RML1CommandFormat {
    public static final String NAME = "Tool speed setting";
    public static final String PATTERN = "VS(" + FLOAT_PATTERN + "|" + INT_PATTERN + ")?;";

    /**
     * Generate the command
     *
     * @param arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, LongArgument.class, FloatArgument.class, 0);
        String ret = null;
        if (arguments == null || arguments.size() == 0) {
            ret = "VS;";
        } else {
            ret = String.format("VS %s;", arguments.get(0).toString());
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        if (arguments.size() == 0) {
            try {
                if (token.contains(".")) {
                    ret = argumentParser.parse(token, FloatArgument.class);
                } else {
                    ret = argumentParser.parse(token, IntArgument.class);
                }
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }
}
