/**
 * ================================================================
 * Copyright (c) 2020-2021 Maiereni Software and Consulting Inc
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
package com.maiereni.cad.rml1.complex;

import com.maiereni.cad.rml1.bo.ComplexArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;

import java.io.StringWriter;

/**
 * A complex command to move and drill at a certain location in absolute coordinates
 *
 * @author Petre Maierean
 */
public class MoveAndDrill extends AbstractComplexCommands {

    /**
     * Generates command. the argument contains the X,Y position of the hole, the Z of the end point. The A is the speed
     * of the spinner
     *
     * @param argument
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(ComplexArgument argument) throws Exception {
        StringWriter sw = new StringWriter();
        sw.write(getInitialize());
        sw.append(getSpeedXY(argument.getSpeedXY()));
        sw.append(getSpeedZ(argument.getSpeedZ()));
        // Move to origin
        sw.write(moveXY(argument.getX(), argument.getY()));
        if (argument.getPoints() != null) {
            for (VertexArgument vertex : argument.getPoints()) {
                sw.write(moveXY(vertex.getX(), vertex.getY()));
                sw.write(drill(argument.getZ(), vertex.getZ()));
            }
        } else {
            throw new Exception("No points have been specified");
        }
        sw.write(getHomeCommand(argument.isReset()));
        sw.write(getMotorOnOff(false));
        return sw.toString();
    }
}
