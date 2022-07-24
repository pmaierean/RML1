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
package com.maiereni.cad.xnc.bo;

import com.maiereni.cad.xnc.Token;

/**
 * @author Petre Maierean
 */
public enum Tokens {
    drill(DrillHole.class),
    endFile(EndFile.class),
    endHeader(EndHeader.class),
    liftTool(LiftRuteToolUp.class),
    metric(MetricUnits.class),
    move(Move.class),
    plunge(PlungeRuteToolDown.class),
    select(SelectTool.class),
    setDrillMode(SetDrillMode.class),
    start(StartHeader.class),
    tool(Tool.class),
    format(Format.class);

    private String pattern;
    private Class<? extends Token> actualToken;

    private Tokens(Class<? extends Token> tokenClass) {
        try {
            pattern = tokenClass.getDeclaredField("PATTERN").get(null).toString();
            actualToken = tokenClass;
        } catch (Exception e) {

        }
    }

    /**
     * Get the associated instance
     *
     * @return
     * @throws Exception
     */
    public Token getInstance() throws Exception {
        return actualToken.getConstructor(null).newInstance(null);
    }

    public String getPattern() {
        return pattern;
    }
}
