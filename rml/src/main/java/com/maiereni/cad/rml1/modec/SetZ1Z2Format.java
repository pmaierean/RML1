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
import com.maiereni.cad.rml1.bo.LongArgument;

import java.util.List;

/**
 * This sets the Z1 and Z2 points in the workpiece coordinate system as
 * relative values from the Z0 coordinate.
 * If an error occurs with either Z1 or Z2, the setting is made for the one for
 * which no error resulted.
 * No setting is made for the one which generated an error.
 * Z2 may be set outside the work area, but in actual operation it is at the
 * topmost location in the work area. (Rising beyond that is physically
 * impossible.)
 * When no parameters are present, the default values are used.
 *
 * @author Petre Maierean
 */
public class SetZ1Z2Format extends RML1CommandFormat {
    public static final String NAME = "Set Z1 and Z2";
    public static final String PATTERN = "!PZ(" + INT_PATTERN + "(\\x2c" + INT_PATTERN + ")?);";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, LongArgument.class, 1);
        String ret = null;
        if (arguments.size() == 1) {
            ret = String.format("!PZ %s;", arguments.get(0).toString());
        } else {
            ret = String.format("!PZ %s,%s;", arguments.get(0).toString(), arguments.get(1).toString());
        }

        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        try {
            String s = token;
            if (!s.startsWith("!PZ")) {
                if (s.endsWith(";")) {
                    s = s.substring(0, s.length() - 1);
                }
                if (s.startsWith(",")) {
                    s = s.substring(1);
                }
                if (s.contains(",")) {
                    String[] toks = s.split(",");
                    s = toks[0].trim();
                }
                ret = argumentParser.parse(s, LongArgument.class);
            }
        } catch (Exception e) {
            logger.error("Failure to parse token '{}' at position {}", token, i, e);
        }
        return ret;


    }

}
