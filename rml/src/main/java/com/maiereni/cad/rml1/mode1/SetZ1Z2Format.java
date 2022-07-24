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
import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.bo.IntArgument;
import com.maiereni.cad.rml1.bo.LongArgument;

import java.util.List;

/**
 * This sets point Z1,Z2 in the workpiece coordinate system. Z1,Z2 specify a relative value from Z0 in the workpiece
 * coordinate system.
 * When Z2 is not specified, the value already in effect is reused.
 * When no parameter exists, the value set using the operation panel may be
 * used, although this varies according to the model.
 * If either Z1 or Z2 (but not both) generates an error, the setting is made for
 * the value that did not generate an error
 *
 * @author Petre Maierean
 */
public class SetZ1Z2Format extends RML1CommandFormat {
    public static final String NAME = "@ (Input Z1 & Z2 command)";
    public static final String PATTERN = "\\x40(" + N_INT_PATTERN + "(\\x2c" + INT_PATTERN + ")?);";

    /**
     * Generates command
     *
     * @param arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, LongArgument.class, 1);
        String ret = null;
        if (arguments.size() == 1) {
            ret = String.format("@ %s;", arguments.get(0));
        } else {
            ret = String.format("@ %s,%s;", arguments.get(0), arguments.get(1));
        }
        return ret;
    }

    @Override
    public List<CommandArgument> parseArguments(String token) throws Exception {
        List<CommandArgument> ret = super.parseArguments(token);
        if (!token.contains(",")) {
            while (ret.size() > 1) {
                ret.remove(ret.size() - 1);
            }
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        try {
            if (arguments.size() < 2) {
                String s = token;
                int ix = s.indexOf(",");
                if (ix > 0) {
                    s = s.substring(0, ix).trim();
                } else if (ix == 0) {
                    s = s.substring(1).trim();
                }
                if (token.matches("(\\d)+")) {
                    ret = argumentParser.parse(s.trim(), IntArgument.class);
                }
            }
        } catch (Exception e) {
            logger.error("Failure to parse token '{}' at position {}", token, i, e);
        }
        return ret;
    }
}
