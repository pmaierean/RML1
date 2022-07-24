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
package com.maiereni.cad.rml1;

import com.maiereni.cad.rml1.bo.Mode1Command;
import com.maiereni.cad.rml1.bo.Mode2Command;
import com.maiereni.cad.rml1.bo.ModeCommonCommand;
import com.maiereni.cad.rml1.mode1.DwellFormat;
import com.maiereni.cad.rml1.mode1.SetZ1Z2Format;
import com.maiereni.cad.rml1.mode1.*;
import com.maiereni.cad.rml1.mode2.*;
import com.maiereni.cad.rml1.modec.*;

/**
 * @author Petre Maierean
 */
public class CommandFormatFactory {

    /**
     * Get Mode1 command
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static RML1CommandFormat getFormat(Mode1Command name) throws Exception {
        RML1CommandFormat ret = null;
        switch (name) {
            case callMode:
                ret = new CallModeFormat();
                break;
            case drawLineCutting:
                ret = new DrawLineCuttingFormat();
            case dwell:
                ret = new DwellFormat();
                break;
            case homeMovement:
                ret = new HomeMovementFormat();
                break;
            case linearMovement:
                ret = new LinearMovementFormat();
                break;
            case relativeDrawLineCutting:
                ret = new RelativeDrawLineCuttingFormat();
                break;
            case relativeLiniarMovement:
                ret = new RelativeLinearMovementFormater();
                break;
            case setVelocity:
                ret = new VelocityFormat();
                break;
            case setZ1Z2:
                ret = new SetZ1Z2Format();
                break;
            case setZVelocity:
                ret = new ZVelocityFormat();
                break;
            case threeAxesMovement:
                ret = new ThreeAxisFormat();
                break;
        }
        return ret;
    }

    /**
     * Get Mode2 command
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static RML1CommandFormat getFormat(Mode2Command name) throws Exception {
        RML1CommandFormat ret = null;
        switch (name) {
            case defaultFormat:
                ret = new DefaultSettingsFormat();
                break;
            case initialize:
                ret = new InitializeFormat();
                break;
            case penDown:
                ret = new PenDownFormat();
                break;
            case penUp:
                ret = new PenUpFormat();
                break;
            case plotAbsolute:
                ret = new PlotAbsoluteFormat();
                break;
            case plotRelative:
                ret = new PlotRelativeFormat();
                break;
            case velocitySelection:
                ret = new VelocitySelectionFormat();
                break;
        }
        return ret;
    }

    /**
     * Get Modec command
     *
     * @param name
     * @return
     * @throws Exception
     */
    public static RML1CommandFormat getFormat(ModeCommonCommand name) throws Exception {
        RML1CommandFormat ret = null;
        switch (name) {
            case dwell:
                ret = new com.maiereni.cad.rml1.modec.DwellFormat();
                break;
            case extensionAxisMovement:
                ret = new ExtensionAxisMovementFormat();
                break;
            case motorControl:
                ret = new MotorControlFormat();
                break;
            case notReady:
                ret = new NotReadyFormat();
                break;
            case revolutionControl:
                ret = new RevolutionControlFormat();
                break;
            case setZ0:
                ret = new SetZ0Format();
                break;
            case setZ1Z2:
                ret = new com.maiereni.cad.rml1.modec.SetZ1Z2Format();
                break;
            case threeAxisFeed:
                ret = new ThreeAxisSimultanousFeedFormat();
                break;
            case velocitySelectZ:
                ret = new VelocitySelectZFormat();
                break;
            case zAxisMovement:
                ret = new ZAxisMoveFormat();
                break;
        }
        return ret;
    }

}
