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
package com.maiereni.cad.xnc;

import com.maiereni.cad.xnc.bo.Tool;

import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author Petre Maierean
 */
public class RN1PrinterStatus {
    public static final NumberFormat NF = NumberFormat.getNumberInstance();
    private float maxX, minX, maxY, minY, currentX, currentY;
    private List<Tool> tools;

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        sw.write("X: ");
        sw.write(NF.format(minX));
        sw.write(" - ");
        sw.write(NF.format(maxX));
        sw.write(" | Y: ");
        sw.write(NF.format(minY));
        sw.write(" - ");
        sw.write(NF.format(maxY));
        if (tools != null) {
            for (Tool tool : tools) {
                sw.write("\n\rTool: ");
                sw.write(tool.toString());
            }
        }
        return sw.toString();
    }
}
