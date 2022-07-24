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
package com.maiereni.cad.rml1.bo;

import com.maiereni.cad.rml1.mode1.*;

/**
 * @author Petre Maierean
 */
public enum Mode1Command {
    callMode(CallModeFormat.NAME, "^"),
    drawLineCutting(DrawLineCuttingFormat.NAME, "D"),
    dwell(DwellFormat.NAME, "W"),
    homeMovement(HomeMovementFormat.NAME, "H"),
    linearMovement(LinearMovementFormat.NAME, "M"),
    relativeDrawLineCutting(RelativeDrawLineCuttingFormat.NAME, "I"),
    relativeLiniarMovement(RelativeLinearMovementFormater.NAME, "R"),
    setVelocity(VelocityFormat.NAME, "F"),
    setZ1Z2(SetZ1Z2Format.NAME, "@"),
    setZVelocity(ZVelocityFormat.NAME, "V"),
    threeAxesMovement(ThreeAxisFormat.NAME, "Z");

    private String name, commandLetter;
    ;

    private Mode1Command(String name, String commandLetter) {
        this.name = name;
        this.commandLetter = commandLetter;
    }

    public String getCommandLetter() {
        return commandLetter;
    }

    public String getName() {
        return name;
    }

}
