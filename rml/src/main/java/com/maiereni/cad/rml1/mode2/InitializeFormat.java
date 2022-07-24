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

import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.CommandArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * This performs the same operations as the standard settings made by the
 * DF command, as well as making the following settings.
 * PA (Plot Absolute -- Absolute plot command)
 * <Format>
 * <Type>
 * PA x1, y1, (, x2, y2,....) [terminator]
 * PA [terminator]
 * x: float
 * y: float
 * <Parameter Range>
 * x: -8388608.0 to 8388607.0
 * y: -8388608.0 to 8388607.0
 * <Description>
 * <Errors>
 * A PA command without parameters effects a change to the absolute
 * coordinate
 * mode.
 * When parameters are present, the command effects the absolute
 * coordinate
 * mode and performs operation for the coordinate values
 * specified by the parameters, without change the present state of the tool
 * (up or down).
 * For the parameters, a pair of values, x and y, constitute a single set, and
 * more than one set may be stated.
 * When the number of parameters is odd, each pair is sequentially
 * interpreted from the beginning as x and y and is executed accordingly, and
 * the final remaining odd value generates error 2. Execution is not
 * performed.
 * - 17 -
 * RML-1 PROGRAMING GUIDELINE
 * 1. Move the tool to the tool-up position.
 * 2. PU.
 * 3. Clear errors.
 * 4. Stop the spindle motor.
 * 5. Reset the coordinate values. The present position of the tool is the
 * same as the position in the workpiece coordinate system.
 *
 * @author Petre Maierean
 */
public class InitializeFormat extends RML1CommandFormat {
    public static final String NAME = "Initialize";
    public static final String PATTERN = "IN;";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        if (arguments != null && arguments.size() > 0) {
            throw new Exception("Unsupported argument(s)");
        }
        return PATTERN;
    }

    /**
     * The formated does not have arguments
     *
     * @param token
     * @return false if token is not null or blank
     */
    @Override
    public boolean isParseable(String token) {
        return StringUtils.isBlank(token);
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        return null;
    }
}
