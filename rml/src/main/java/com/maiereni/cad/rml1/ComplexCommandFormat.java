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
package com.maiereni.cad.rml1;

import com.maiereni.cad.rml1.bo.ComplexArgument;

/**
 * Defines the API of a specialized format utility to make a command in RML-1 for a certain operation
 *
 * @author Petre Maierean
 */
public interface ComplexCommandFormat {
    /**
     * Generates a complex command based on the argument
     *
     * @param argument
     * @return a string containing commands
     * @throws Exception
     */
    String generateCommand(ComplexArgument argument) throws Exception;
}
