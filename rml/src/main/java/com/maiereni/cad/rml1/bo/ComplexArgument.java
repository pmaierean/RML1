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
package com.maiereni.cad.rml1.bo;

import java.util.List;

/**
 * @author Petre Maierean
 */
public class ComplexArgument {
    private Float x, y, z, speedXY, speedZ, speedSpinner;
    private List<VertexArgument> points;
    private boolean reset;

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

    public Float getSpeedXY() {
        return speedXY;
    }

    public void setSpeedXY(Float speedXY) {
        this.speedXY = speedXY;
    }

    public Float getSpeedZ() {
        return speedZ;
    }

    public void setSpeedZ(Float speedZ) {
        this.speedZ = speedZ;
    }

    public Float getSpeedSpinner() {
        return speedSpinner;
    }

    public void setSpeedSpinner(Float speedSpinner) {
        this.speedSpinner = speedSpinner;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public List<VertexArgument> getPoints() {
        return points;
    }

    public void setPoints(List<VertexArgument> points) {
        this.points = points;
    }
}
