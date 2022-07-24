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

import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.IntArgument;

import java.util.List;

/**
 * When a parameter is given, if it is a settable value, then dwell (waiting) for
 * the time set by the value is performed when the cutting direction changes.
 * When W is used without a parameter, the default value is applied (0
 * msec).
 * There is no wait time when the direction of cutting changes.
 * When a two-dimensional command such as PD or PU is used, the dwell
 * operation is performed just before the PD tool-down operation or just
 * before moving the XY plane period parameters.
 * It is also performed just before changing from tool-down to tool-up with
 * PU.
 * However, it is not performed when a PU parameter manipulates the XY
 * plane.
 * In the case of a PU or PD command without parameters, it is performed
 * just before the tool-up or tool-down operation.
 * In the case of a three-dimensional movement command such as !ZZ, it is
 * performed just before the operation of that command is carried out.
 * Operations other than the command's are unaffected.
 *
 * @author Petre Maierean
 */
public class DwellFormat extends RML1CommandFormat {
    public static final String NAME = "Dwell";
    public static final String PATTERN = "W(" + INT_PATTERN + ")?;";

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
            ret = "W;";
        } else {
            ret = String.format("W %s;", arguments.get(0).toString());
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        logger.debug("Token {}='{}'", i, token);
        if (arguments.size() == 0) {
            try {
                ret = argumentParser.parse(token, IntArgument.class);
            } catch (Exception e) {
                logger.error("Failure to parse token '{}' at position {}", token, i, e);
            }
        }
        return ret;
    }
}
