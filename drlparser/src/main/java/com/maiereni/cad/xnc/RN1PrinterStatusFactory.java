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

import com.maiereni.cad.xnc.bo.DrillHole;
import com.maiereni.cad.xnc.bo.Move;
import com.maiereni.cad.xnc.bo.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petre Maierean
 */
public class RN1PrinterStatusFactory {

    /**
     * Get and instance of the factory
     *
     * @return an instance of the factory
     */
    public static RN1PrinterStatusFactory get() {
        return new RN1PrinterStatusFactory();
    }

    /**
     * Gets a RN1 Printer status for the toolpath
     *
     * @param tokens
     * @return
     */
    public RN1PrinterStatus getInitialized(List<Token> tokens) {
        RN1PrinterStatus ret = new RN1PrinterStatus();
        float maxX = 0, minX = 0, maxY = 0, minY = 0;
        if (tokens != null) {
            List<Tool> tools = new ArrayList<>();
            for (Token token : tokens) {
                if (token instanceof DrillHole) {
                    DrillHole drillHole = (DrillHole) token;
                    update(ret, drillHole.getX(), drillHole.getY());
                } else if (token instanceof Move) {
                    Move move = (Move) token;
                    update(ret, move.getX(), move.getY());
                } else if (token instanceof Tool) {
                    Tool tool = (Tool) token;
                    tools.add(tool);
                }
            }
            if (!tools.isEmpty()) {
                ret.setTools(tools);
            }
        }
        return ret;
    }

    private void update(RN1PrinterStatus status, float x, float y) {
        if (x > status.getMaxX()) {
            status.setMaxX(x);
        }
        if (x < status.getMinX()) {
            status.setMinX(x);
        }
        if (y > status.getMaxY()) {
            status.setMaxY(y);
        }
        if (y < status.getMinY()) {
            status.setMinY(y);
        }
    }
}
