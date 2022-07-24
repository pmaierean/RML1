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

import com.maiereni.cad.rml1.modec.*;

/**
 * @author Petre Maierean
 */
public enum ModeCommonCommand {
    dwell(DwellFormat.NAME, "!DW"),
    extensionAxisMovement(ExtensionAxisMovementFormat.NAME, "!ZE"),
    motorControl(MotorControlFormat.NAME, "!MC"),
    notReady(NotReadyFormat.NAME, "!NR"),
    revolutionControl(RevolutionControlFormat.NAME, "!RC"),
    setZ0(SetZ0Format.NAME, "!ZO"),
    setZ1Z2(SetZ1Z2Format.NAME, "!PZ"),
    threeAxisFeed(ThreeAxisSimultanousFeedFormat.NAME, "!ZZ"),
    velocitySelectZ(VelocitySelectZFormat.NAME, "!VZ"),
    zAxisMovement(ZAxisMoveFormat.NAME, "!ZM");

    private String name, commandLetter;

    private ModeCommonCommand(String name, String commandLetter) {
        this.commandLetter = commandLetter;
        this.name = name;
    }

    public String getCommandLetter() {
        return commandLetter;
    }

    public String getName() {
        return name;
    }
}
