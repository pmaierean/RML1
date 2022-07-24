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
import com.maiereni.cad.rml1.bo.FloatArgument;
import com.maiereni.cad.rml1.bo.LongArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * This sets the speed relationship when the tool moves along the Z axis or
 * when movement is performed using the three-axis simultaneousmovement
 * command.
 * !VZ without a parameter is set to the default speed.
 * When a parameter is present and is a settable speed, the speed is set to
 * the value of the parameter.
 * When not a settable value, the maximum or minimum settable value is set
 * automatically.
 * On some models, !VZ 0 may be, for example, 0.5 mm/sec.
 * The default value may be, for example, 2 mm/sec.
 *
 * @author Petre Maierean
 */
public class VelocitySelectZFormat extends RML1CommandFormat {
    public static final String NAME = "Set velocity on Z axis";
    public static final String PATTERN = "!VZ(" + INT_PATTERN + "|" + FLOAT_PATTERN + ")?;";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, LongArgument.class, FloatArgument.class, 0);
        String ret = null;
        if (arguments == null || arguments.size() == 0) {
            ret = "!VZ;";
        } else {
            ret = String.format("!VZ %s;", arguments.get(0).toString());
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
                if (s.startsWith("!VZ")) {
                    s = s.substring(3).trim();
                }
                if (s.endsWith(";")) {
                    s = s.substring(0, s.length() - 1);
                }
                String[] t = s.split(",");
                s = t[0];
                if (StringUtils.isNotBlank(s)) {
                    if (s.contains(".")) {
                        ret = argumentParser.parse(s, FloatArgument.class);
                    } else {
                        ret = argumentParser.parse(s, LongArgument.class);
                    }
                }
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }
}
