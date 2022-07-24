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


import java.util.List;

/**
 * @author Petre Maierean
 */
public class Description {
    private List<FormaterCfg> mode1, mode2, modec;

    public List<FormaterCfg> getMode1() {
        return mode1;
    }

    public void setMode1(List<FormaterCfg> mode1) {
        this.mode1 = mode1;
    }

    public List<FormaterCfg> getMode2() {
        return mode2;
    }

    public void setMode2(List<FormaterCfg> mode2) {
        this.mode2 = mode2;
    }

    public List<FormaterCfg> getModec() {
        return modec;
    }

    public void setModec(List<FormaterCfg> modec) {
        this.modec = modec;
    }
}
