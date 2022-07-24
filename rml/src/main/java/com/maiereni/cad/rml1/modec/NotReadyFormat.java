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
package com.maiereni.cad.rml1.modec;

import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.CommandArgument;

import java.util.List;

/**
 * This effects a paused state.
 * No parameter is required.
 *
 * @author Petre Maierean
 */
public class NotReadyFormat extends RML1CommandFormat {
    public static final String NAME = "Not ready";
    public static final String PATTERN = "!NR;";

    /**
     * Generate command
     *
     * @param arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        return PATTERN;
    }


    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        return null;
    }
}
