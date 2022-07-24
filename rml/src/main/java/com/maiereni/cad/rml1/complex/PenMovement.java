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
import java.math.BigDecimal;
import java.util.List;

/**
 * Generates a series of PU x,y commands according to the generation plan
 *
 * @author Petre Maierean
 */
public class PenMovement extends AbstractComplexCommands {

    @Override
    public String generateCommand(ComplexArgument argument) throws Exception {
        if (argument == null) {
            throw new Exception("The argument cannot be null");
        }
        Long z1 = getZ1(argument.getPoints());
        Long z2 = convert(argument.getZ());
        StringWriter sw = new StringWriter();
        sw.write(getInitialize());
        sw.write(getSpeedXY(argument.getSpeedXY()));
        sw.write(getSpeedZ(argument.getSpeedZ()));
        sw.write(setZ1Z2(z1, z2));
        if (argument.getPoints() != null) {
            for (VertexArgument vertex : argument.getPoints()) {
                sw.write(getPenUpCommand(vertex));
                sw.write(getPenDownCommand(null));
            }
            sw.write(getPenUpCommand(null));
        } else {
            throw new Exception("No points have been specified");
        }
        sw.write(getHomeCommand(argument.isReset()));
        sw.write(getMotorOnOff(false));
        return sw.toString();
    }

    private Long getZ1(List<VertexArgument> vertexes) throws Exception {
        if (vertexes == null) {
            throw new Exception("No points to draw a command path were found");
        }
        float f = 0;
        for (VertexArgument vertex : vertexes) {
            if (vertex.getZ() != 0) {
                if (f == 0) {
                    f = vertex.getZ();
                } else if (f != vertex.getZ()) {
                    throw new Exception("The list of vertexes do not have the same Z position");
                }
            }
        }
        return convert(f);
    }

    private Long convert(float f) {
        BigDecimal bd = new BigDecimal(f);
        return bd.longValue();
    }

    private Long convert(Float f) {
        return f == null ? 0 : convert(f.floatValue());
    }

}
