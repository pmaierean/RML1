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
import com.maiereni.cad.rml1.bo.FloatArgument;

import java.util.List;

/**
 * Coordinate values are specified as machine coordinates.
 * If a PA command was executed before this command, the z parameter is
 * interpreted as an absolute coordinate, and the specified value from the
 * device's reference coordinate is taken to be the Z-axis origin point,
 * regardless of where the present Z-axis origin point (Z0) may be.
 * Conversely, if a PR command was executed before this command, the
 * parameter is interpreted as a relative coordinate, and the specified value
 * referenced from the present Z-axis coordinate is made the Z-axis origin.
 * When no command has been received, the parameter is interpreted as an
 * absolute coordinate.
 * It is also interpreted as an absolute coordinate after an IN or DF command.
 * When no parameter is present, the default value is used.
 * The default is model-dependent.
 *
 * @author Petre Maierean
 */
public class SetZ0Format extends RML1CommandFormat {
    public static final String NAME = "Set Z0";
    public static final String PATTERN = "!ZO(" + FLOAT_PATTERN + ")?;";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, FloatArgument.class, 0);
        String ret = null;
        if (arguments == null || arguments.size() == 0) {
            ret = "!ZO;";
        } else {
            ret = String.format("!ZO %s;", arguments.get(0).toString());
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        if (arguments.size() == 0) {
            try {
                String s = token;
                if (s.startsWith("!ZO")) {
                    s = s.substring(3).trim();
                }
                if (s.endsWith(";")) {
                    s = s.substring(0, s.length() - 1);
                }
                ret = argumentParser.parse(s, FloatArgument.class);
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }

}
