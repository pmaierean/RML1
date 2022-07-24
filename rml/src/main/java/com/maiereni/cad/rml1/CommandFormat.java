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
package com.maiereni.cad.rml1;

import java.util.List;
import java.util.Locale;

/**
 * The base class representing a command in the RML-1 package
 *
 * @author Petre Maierean
 */
public interface CommandFormat {
    /**
     * Convert a list of arguments to a command token
     *
     * @param arguments the arguments
     * @return
     * @throws Exception
     */
    String generateCommand(List<CommandArgument> arguments) throws Exception;

    /**
     * Given a string token, verify if it is parseable
     *
     * @param token
     * @return
     */
    boolean isParseable(String token);

    /**
     * Given a string token, parse the arguments
     *
     * @param token a string token
     * @return a list of arguments
     * @throws Exception cannot parse the arguments
     */
    List<CommandArgument> parseArguments(String token) throws Exception;

    /**
     * Provides a description for the command
     *
     * @param locale
     * @param arguments
     * @return
     */
    String describeCommand(Locale locale, List<CommandArgument> arguments);
}
