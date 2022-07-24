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

import com.maiereni.cad.rml1.CommandArgument;

import java.io.StringWriter;

/**
 * @author Petre Maierean
 */
public class AxisArgument implements CommandArgument {
    private Float x, y, z, a;

    public AxisArgument() {
    }

    public AxisArgument(Float x, Float y, Float z, Float a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Float getA() {
        return a;
    }

    public void setA(Float a) {
        this.a = a;
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        if (x != null) {
            sw.write("X");
            sw.write(FloatArgument.NUMBER_FORMAT.format(x.floatValue()));
        }
        if (y != null) {
            sw.write("Y");
            sw.write(FloatArgument.NUMBER_FORMAT.format(y.floatValue()));
        }
        if (z != null) {
            sw.write("Z");
            sw.write(FloatArgument.NUMBER_FORMAT.format(z.floatValue()));
        }
        if (a != null) {
            sw.write("A");
            sw.write(FloatArgument.NUMBER_FORMAT.format(a.floatValue()));
        }
        return sw.toString();
    }
}
