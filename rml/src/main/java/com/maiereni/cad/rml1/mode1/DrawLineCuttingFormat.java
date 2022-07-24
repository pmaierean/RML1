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
package com.maiereni.cad.rml1.mode1;

import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.PairOfFloatsArgument;

import java.io.StringWriter;
import java.util.List;

/**
 * This sequentially cuts line segments between coordinates specified from
 * the present tool location, in the sequence x1,y1, x2,y2,..., xn,yn. The
 * coordinate values are all absolute coordinates. Thereafter the system is in
 * the absolute-coordinate mode.
 *
 * @author Petre Maierean
 */
public class DrawLineCuttingFormat extends RML1CommandFormat {
    public static final String NAME = "Draw -- Line-cutting command";
    public static final String PATTERN = "D(" + PAIR_OF_FLOATS_PATTERN + "(\\x2c" + PAIR_OF_FLOATS_PATTERN + ")*" + ")?;";

    protected String getCommandLetter() {
        return "D";
    }

    /**
     * Generates command
     *
     * @param arguments
     * @return
     * @throws Exception
     */
    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, PairOfFloatsArgument.class, 0);
        StringWriter sw = new StringWriter();
        sw.write(getCommandLetter());
        if (arguments != null) {
            boolean hasComma = false;
            for (CommandArgument argument : arguments) {
                if (hasComma) {
                    sw.write(",");
                } else {
                    sw.write(" ");
                }
                PairOfFloatsArgument pairOfFloatsArgument = (PairOfFloatsArgument) argument;
                sw.write(pairOfFloatsArgument.toString());
                hasComma = true;
            }
        }
        sw.write(COMMAND_TEMINATOR);
        return sw.toString();
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        CommandArgument ret = null;
        try {
            String s = token;
            if (arguments.size() > 0) {
                if (s.startsWith(",")) {
                    s = s.substring(1);
                }
            }
            ret = argumentParser.parse(s, PairOfFloatsArgument.class);
        } catch (Exception e) {
            logger.error("Failure to parse token '{}' at position {}", token, i, e);
        }
        return ret;
    }

}
