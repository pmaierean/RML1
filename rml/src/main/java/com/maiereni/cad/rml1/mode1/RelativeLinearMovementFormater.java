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

import java.util.Locale;

/**
 * This performs movement at the Z2 height from the present tool position to
 * the coordinates specified by the amount of change along the X axis dx1
 * and the amount of change along the Y axis dy1.
 * It next performs movement at the Z2 height from the position moved to, to
 * the coordinates specified by the amounts of change dy1, dy2 for the
 * corresponding X and Y axes. This sequentially performs movement up
 * through the final specified coordinates.
 * It performs linear movement between the coordinates.
 * Movement is generally at the highest speed of the particular model.
 * The coordinate values are all relative coordinates, and the system goes
 * into the relative-coordinate mode.
 * When the machine is at a height other than Z2 when first moving to the
 * amounts of change dx1, dy1, the spindle is rotated if it is in a rotatable
 * state, and after rotation stabilizes, movement to the Z2 height is effected at
 * the speed specified by !VZ or V.
 * Thereafter movement to the coordinates specified by dx1, dy1 is effected.
 * In cases where this command appears when the height is already at Z2,
 * the spindle is rotated if it is in a rotatable state, and after rotation stabilizes,
 * movement directly to the coordinates specified by dx1, dy1 is effected.
 *
 * @author Petre Maierean
 */
public class RelativeLinearMovementFormater extends DrawLineCuttingFormat {
    public static final String NAME = "Relative Movements At Z2";
    public static final String PATTERN = "R(" + PAIR_OF_FLOATS_PATTERN + "(\\x2c" + PAIR_OF_FLOATS_PATTERN + ")*" + ")?;";

    @Override
    protected String getCommandLetter() {
        return "R";
    }
}
