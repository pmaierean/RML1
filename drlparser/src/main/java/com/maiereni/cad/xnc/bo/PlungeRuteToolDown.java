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
public class PlungeRuteToolDown implements Token {
    public static final String PATTERN = "M15";

    @Override
    public String getPattern() {
        return PATTERN;
    }

    public String toString() {
        return PATTERN;
    }

}

