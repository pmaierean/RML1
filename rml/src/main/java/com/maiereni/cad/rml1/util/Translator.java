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
package com.maiereni.cad.rml1.util;

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.CommandFormat;
import com.maiereni.cad.rml1.CommandFormatFactory;
import com.maiereni.cad.rml1.bo.Mode1Command;
import com.maiereni.cad.rml1.bo.ModeCommonCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A utility class which translates to readable text the instructions sent to an RML-1 able printer
 *
 * @author Petre Maierean
 */
public class Translator {
    protected static final Logger logger = LogManager.getLogger(Translator.class);

    /**
     * Translate a set of instructions to readable text
     *
     * @param instructions
     * @param locale
     * @return
     * @throws Exception
     */
    public String translate(String instructions, Locale locale) throws Exception {
        List<String> arr = getTranslation(instructions, locale);
        StringWriter sw = new StringWriter();
        for (String s : arr) {
            sw.write(s);
            sw.write("\r\n");
        }
        return sw.toString();
    }

    /**
     * Convert the content of a file into a readable text
     *
     * @param pn
     * @param locale
     * @return
     * @throws Exception
     */
    public String translate(File pn, Locale locale) throws Exception {
        List<String> arr = getTranslation(pn, locale);
        StringWriter sw = new StringWriter();
        for (String s : arr) {
            sw.write(s);
            sw.write("\r\n");
        }
        return sw.toString();
    }

    /**
     * Translate all the instructions of a file
     *
     * @param pn
     * @param locale
     * @return
     * @throws Exception
     */
    public List<String> getTranslation(File pn, Locale locale) throws Exception {
        List<String> ret = new ArrayList<>();
        if (pn == null || !pn.isFile()) {
            throw new Exception("Invalid argument");
        }
        try (FileReader fr = new FileReader(pn);
             LineNumberReader lnr = new LineNumberReader(fr);) {
            String s = null;
            for (int lineNumber = 1; (s = lnr.readLine()) != null; lineNumber++) {
                try {
                    String comment = "// (" + lineNumber + ") " + s;
                    ret.add(comment);
                    if (!s.endsWith(";")) {
                        s = s + ";";
                    }

                    List<String> translation = getTranslation(s, locale);
                    ret.addAll(translation);
                } catch (Exception e) {
                    logger.error("Failed to parse string '" + s + "' at line " + lineNumber);
                    throw e;
                }
            }
        }
        return ret;
    }

    /**
     * Given a set of instruction pased in the arguments, translate them to a readable text
     *
     * @param instructions
     * @param locale
     * @return
     * @throws Exception
     */
    public List<String> getTranslation(String instructions, Locale locale) throws Exception {
        List<String> ret = new ArrayList<>();
        if (StringUtils.isNotBlank(instructions)) {
            try (StringReader sr = new StringReader(instructions);
                 LineNumberReader lnr = new LineNumberReader(sr)) {
                String sl = null;
                while ((sl = lnr.readLine()) != null) {
                    if (!sl.endsWith(";")) {
                        sl = sl + ";";
                    }
                    int begin = 0;
                    for (int i = 0; i < sl.length(); i++) {
                        if (sl.charAt(i) == ';') {
                            if (i > begin) {
                                String token = sl.substring(begin, i);
                                if (token.startsWith(";")) {
                                    begin = i + 1;
                                    continue;
                                }
                                CommandFormat commandFormat = null;
                                if (((commandFormat = getMode1Command(token)) != null) ||
                                        ((commandFormat = getModeCommonCommand(token)) != null)) {
                                    if (!token.endsWith(";")) {
                                        token = token + ";";
                                    }
                                    List<CommandArgument> arguments = commandFormat.parseArguments(token);
                                    begin = i + 1;
                                    String s = commandFormat.describeCommand(locale, arguments);
                                    ret.add(s);
                                    continue;
                                }
                                throw new Exception("Could not parse the input string at (" + begin + ", " + i + ") '" + sl + "'");
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    private CommandFormat getMode1Command(String token) throws Exception {
        CommandFormat ret = null;
        for (Mode1Command mode1Command : Mode1Command.values()) {
            if (token.startsWith(mode1Command.getCommandLetter())) {
                ret = CommandFormatFactory.getFormat(mode1Command);
                break;
            }
        }
        return ret;
    }

    private CommandFormat getModeCommonCommand(String token) throws Exception {
        CommandFormat ret = null;
        for (ModeCommonCommand modeCommonCommand : ModeCommonCommand.values()) {
            if (token.startsWith(modeCommonCommand.getCommandLetter())) {
                ret = CommandFormatFactory.getFormat(modeCommonCommand);
                break;
            }
        }
        return ret;
    }
}
