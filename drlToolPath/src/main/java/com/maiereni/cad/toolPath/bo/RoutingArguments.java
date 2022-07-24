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
package com.maiereni.cad.toolPath.bo;

/**
 * Contains the routing arguments
 *
 * @author Petre Maierean
 */
public class RoutingArguments {
    private float z0, z1, offsetX, offsetY, unitConversionRate;
    private Float speedZ;
    private int forStepping;
    private boolean writeExtremes;

    /**
     * Get the Z0 position
     *
     * @return
     */
    public float getZ0() {
        return z0;
    }

    public void setZ0(float z0) {
        this.z0 = z0;
    }

    /**
     * Get the Z1 position
     *
     * @return
     */
    public float getZ1() {
        return z1;
    }

    public void setZ1(float z1) {
        this.z1 = z1;
    }

    /**
     * Get the X axis offset to use for calculating the output x
     *
     * @return
     */
    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * Get the Y axis offset to use for calculating the output y
     *
     * @return
     */
    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * Get to conversion rate to use for calculating both output x and y coordinates
     *
     * @return
     */
    public float getUnitConversionRate() {
        return unitConversionRate;
    }

    public void setUnitConversionRate(float unitConversionRate) {
        this.unitConversionRate = unitConversionRate;
    }

    /**
     * Get the maximum number of steps to write in one drilling instruction. If 0 or less is passed than the
     * output contains one drilling instruction per line
     *
     * @return
     */
    public int getForStepping() {
        return forStepping;
    }

    public void setForStepping(int forStepping) {
        this.forStepping = forStepping;
    }

    public Float getSpeedZ() {
        return speedZ;
    }

    public void setSpeedZ(Float speedZ) {
        this.speedZ = speedZ;
    }

    public boolean isWriteExtremes() {
        return writeExtremes;
    }

    public void setWriteExtremes(boolean writeExtremes) {
        this.writeExtremes = writeExtremes;
    }
}
