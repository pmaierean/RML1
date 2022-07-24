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
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.LongArgument;

import java.util.List;

/**
 * @author Petre Maierean
 */
public class ZVelocityFormat extends VelocityFormat {
    public static final String NAME = "Set velocity for Z movements";
    public static final String PATTERN = "V(" + INT_PATTERN + "|" + FLOAT_PATTERN + ")?;";

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
            ret = "V;";
        } else {
            ret = String.format("V %s;", arguments.get(0).toString());
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
                    ret = argumentParser.parse(token, LongArgument.class);
                }
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }
}
