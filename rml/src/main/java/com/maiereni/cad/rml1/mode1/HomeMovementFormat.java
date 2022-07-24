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

import java.util.List;

/**
 * This moves the tool to the uppermost position, then moves to the
 * workpiece-coordinate origin point on the XY plane. It then stops the
 * spindle motor.
 * If the workpiece-coordinate origin point is outside the cutting range, then
 * this moves, for example, to the clip location.
 * -> Command errors are cleared.
 * -> After the operation, the settings for tool-up, absolutecoordinate
 * mode, and coordinate values are reset.
 * The present position of the tool is the same as the position in the
 * workpiece coordinate system.
 *
 * @author Petre Maierean
 */
public class HomeMovementFormat extends RML1CommandFormat {
    public static final String NAME = "Move home";
    public static final String PATTERN = "H(\\x20)?;";

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, 0);
        return "H;";
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        return null;
    }
}
