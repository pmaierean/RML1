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
import com.maiereni.cad.rml1.bo.AxisArgument;

import java.io.StringWriter;
import java.util.List;

/**
 * This moves (or rotates) the specified axis from the present coordinate or
 * angle to the specified coordinate or angle.
 * Each parameter that follows !ZE is given in a format composed of a letter
 * of the alphabet indicating the name of an axis and a numerical value
 * indicating the movement-destination coordinate.
 *
 * @author Petre Maierean
 */
public class ExtensionAxisMovementFormat extends RML1CommandFormat {
    public static final String NAME = "Extension axis move";
    public static final String SPECIAL_CHARACTERS_TO_AVOID = "[b-v|B-V|\\x40].*";

    public static final String PATTERN = "!ZE\\x20[" + X_PATTERN + "|" + Y_PATTERN + Z_PATTERN + "|" + A_PATTERN + "]*;";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, AxisArgument.class, 1);
        StringWriter sw = new StringWriter();
        sw.write("!ZE ");
        boolean b = false;
        for (CommandArgument arg : arguments) {
            if (b) {
                sw.append(":");
            }
            AxisArgument argument = (AxisArgument) arg;
            sw.append(argument.toString());
            b = true;
        }
        sw.write(COMMAND_TEMINATOR);
        return sw.toString();
    }

    @Override
    protected String getSpecialCharactersToAvoid() {
        return SPECIAL_CHARACTERS_TO_AVOID;
    }

    @Override
    protected CommandArgument getSingleParameter(String s) {
        CommandArgument ret = null;
        try {
            ret = argumentParser.parse(s, AxisArgument.class);
        } catch (Exception e) {
            logger.error("Failed to parse single parameter {}", s, e);
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        logger.debug("Token {}='{}'", i, token);
        try {

        } catch (Exception e) {
            logger.error("Failure to parse token '{}' at position {}", token, i, e);
        }
        return ret;
    }
}
