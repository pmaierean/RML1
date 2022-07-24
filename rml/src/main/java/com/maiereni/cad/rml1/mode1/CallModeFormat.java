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

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.CommandFormat;
import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.bo.CallArgument;
import com.maiereni.cad.rml1.mode2.*;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This calls a mode 2 command from mode 1.
 * For parameters, refer to the description for the called mode 2 command.
 * When only "^" is given, this instruction set does not end until a mode 2
 * command is executed or until a numeral, sign, or other such data that
 * results in a mode 2 error is encountered.
 *
 * @author Petre Maierean
 */
public class CallModeFormat extends RML1CommandFormat {
    public static final String NAME = "Call Mode 2";
    public static final String PATTERN = "\\x5E(\\x20)?[DF|IN|PA|PD|PR|PU|VS].*;";
    private static final String NO_PARAMETERS = "\\x5E(\\x20)?(DF|IN)(\\x20)?;";

    /**
     * Provides a description for the command
     *
     * @param locale
     * @param arguments
     * @return
     */
    @Override
    public String describeCommand(Locale locale, List<CommandArgument> arguments) {
        StringWriter sw = new StringWriter();
        sw.write(NAME);
        sw.write(": ");
        if (arguments.size() > 0) {
            CallArgument argument = (CallArgument) arguments.get(0);
            try {
                CommandFormat commandFormat = getCommandFormat(argument);
                arguments.remove(0);
                sw.write(commandFormat.describeCommand(locale, arguments));
            } catch (Exception e) {
                logger.error("Failed to get command format for {}", argument, e);
            }
        }
        return sw.toString();
    }

    @Override
    public String generateCommand(List<CommandArgument> arguments) throws Exception {
        validateArguments(arguments, 1);
        validate(arguments.get(0), CallArgument.class);
        List<CommandArgument> nestedArgs = new ArrayList<>();
        if (arguments.size() > 1) {
            for (int i = 1; i < arguments.size(); i++) {
                nestedArgs.add(arguments.get(i));
            }
        }
        StringWriter sw = new StringWriter();
        sw.write("^ ");
        CommandFormat commandFormat = getCommandFormat((CallArgument) arguments.get(0));
        String s = commandFormat.generateCommand(nestedArgs);
        sw.write(s);
        return sw.toString();
    }

    @Override
    public List<CommandArgument> parseArguments(String token) throws Exception {
        if (!isParseable(token)) {
            throw new Exception("Cannot parse token " + token);
        }
        List<CommandArgument> ret = new ArrayList<>();
        try {
            String s = token.substring(1).trim();
            if (s.length() > 2) {
                String param = s.substring(2).trim();
                s = s.substring(0, 2);
                CallArgument callArgument = (CallArgument) argumentParser.parse(s, CallArgument.class);
                if (callArgument == null) {
                    throw new Exception("Cannot find a mode2 command for the token");
                }
                CommandFormat commandFormat = getCommandFormat(callArgument);
                ret.add(callArgument);
                if (!token.matches(NO_PARAMETERS)) {
                    List<CommandArgument> args = commandFormat.parseArguments(s + param);
                    ret.addAll(args);
                }
            } else {
                CallArgument callArgument = (CallArgument) argumentParser.parse(s, CallArgument.class);
                ret.add(callArgument);
            }
        } catch (Exception e) {
            logger.error("Failure to parse token '{}'", token, e);
        }
        return ret;
    }

    @Override
    protected CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i) {
        return null;
    }

    private CommandFormat getCommandFormat(CallArgument callArgument) throws Exception {
        CommandFormat ret = null;
        switch (callArgument.getMode2Command()) {
            case defaultFormat:
                ret = new DefaultSettingsFormat();
                break;
            case initialize:
                ret = new InitializeFormat();
                break;
            case penDown:
                ret = new PenDownFormat();
                break;
            case penUp:
                ret = new PenUpFormat();
                break;
            case plotAbsolute:
                ret = new PlotAbsoluteFormat();
                break;
            case plotRelative:
                ret = new PlotRelativeFormat();
                break;
            case velocitySelection:
                ret = new VelocitySelectionFormat();
                break;
        }
        return ret;
    }
}
