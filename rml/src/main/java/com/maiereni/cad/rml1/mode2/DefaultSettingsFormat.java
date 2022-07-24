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
package com.maiereni.cad.rml1.mode2;

import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.CommandArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * This sets some present settings to their standard values.
 * 1. PA This is set to absolute coordinates.
 * 2. VS Speed is set to the default value.
 * The actual value is model-dependent.
 * 3. !VZ Speed is set to the default value.
 * The actual value is model-dependent.
 * 4. !DW Dwell time is set to 0.
 * 5. !MC Rotation is permitted.
 * 6. !PZ The value is initialized. The actual value varies
 * according to the model.
 *
 * @author Petre Maierean
 */
public class DefaultSettingsFormat extends RML1CommandFormat {
    public static final String NAME = "Default Settings";
    public static final String PATTERN = "DF;";

    /**
     * Generates a command
     *
     * @param arguments the arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        if (arguments != null && arguments.size() > 0) {
            throw new Exception("Unsupported argument(s)");
        }
        return PATTERN;
    }

    /**
     * The formated does not have arguments
     *
     * @param token
     * @return false if token is not null or blank
     */
    @Override
    public boolean isParseable(String token) {
        return StringUtils.isBlank(token);
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        return null;
    }

}
