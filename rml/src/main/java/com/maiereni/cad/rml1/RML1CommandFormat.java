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

import com.maiereni.cad.rml1.bo.Description;
import com.maiereni.cad.rml1.bo.FormaterCfg;
import com.maiereni.cad.rml1.util.ArgumentParser;
import com.maiereni.cad.rml1.util.ArgumentValidator;
import com.maiereni.cad.rml1.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Super class for the mode1 set
 *
 * @author Petre Maierean
 */
public abstract class RML1CommandFormat implements CommandFormat {
    protected Logger logger = LogManager.getLogger(getClass());
    public static final String CHARACTERS_TO_AVOID = "[a-z|A-Z|\\x40].*";
    public static final String INT_PATTERN = "(\\x20)?(\\d)+(\\x20)?";
    public static final String N_INT_PATTERN = "(\\x20)?(\\x2d)?(\\d)+(\\x20)?";
    public static final String FLOAT_PATTERN = "(\\x20)?(\\x2d)?(\\d)+(\\x2e\\d*)?(\\x20)?";
    public static final String PAIR_OF_FLOATS_PATTERN = "(" + FLOAT_PATTERN + ")\\x2c(" + FLOAT_PATTERN + ")";
    public static final String THREE_FLOATS_PATTERN = "(" + FLOAT_PATTERN + ")\\x2c(" + FLOAT_PATTERN + ")\\x2c(" + FLOAT_PATTERN + ")";
    public static final String MORE_PAIR_OF_FLOATS_PATTERN = PAIR_OF_FLOATS_PATTERN + ".*";
    public static final Pattern MORE = Pattern.compile(MORE_PAIR_OF_FLOATS_PATTERN);
    private static final String FLOAT = "(\\d)+(\\x2e\\d)?";
    public static final String A_PATTERN = "(\\x20)?(A" + FLOAT + ")(\\x20)?";
    public static final String ANY_A_PATTERN = ".*" + A_PATTERN + ".*";
    public static final String X_PATTERN = "(\\x20)?(X" + FLOAT + ")(\\x20)?";
    public static final String ANY_X_PATTERN = ".*" + X_PATTERN + ".*";
    public static final String Y_PATTERN = "(\\x20)?(Y" + FLOAT + ")(\\x20)?";
    public static final String ANY_Y_PATTERN = ".*" + Y_PATTERN + ".*";
    public static final String Z_PATTERN = "(\\x20)?(Z" + FLOAT + ")(\\x20)?";
    public static final String ANY_Z_PATTERN = ".*" + Z_PATTERN + ".*";
    public static final String COMMAND_TEMINATOR = ";";
    protected ArgumentParser argumentParser = new ArgumentParser();
    protected ArgumentValidator argumentValidator = new ArgumentValidator();

    /**
     * Provides a description for the command
     *
     * @param locale
     * @param arguments
     * @return
     */
    @Override
    public String describeCommand(Locale locale, List<CommandArgument> arguments) {
        String name = getStaticFieldValue("NAME");
        FormaterCfg cfg = name != null ? getFormaterCfg(name, locale) : null;
        StringWriter sw = new StringWriter();
        sw.write(getStaticFieldValue("NAME"));
        sw.write(" (");
        if (arguments != null) {
            boolean comma = false;
            for (CommandArgument commandArgument : arguments) {
                if (comma) {
                    sw.write(",");
                }
                sw.write(commandArgument.toString());
                comma = true;
            }
        }
        sw.write(");");
        if (cfg != null) {
            sw.write("//");
            sw.write(cfg.getDescription());
        }
        return sw.toString();
    }

    /**
     * Given a string token, verify if it is parseable
     *
     * @param token
     * @return
     */
    @Override
    public boolean isParseable(String token) {
        boolean ret = false;
        String pattern = getStaticFieldValue("PATTERN");
        if (StringUtils.isNoneBlank(token, pattern) && token.matches(pattern)) {
            ret = true;
        }
        return ret;
    }

    /**
     * Given a string token, parse the arguments
     *
     * @param token a string token
     * @return a list of arguments
     * @throws Exception cannot parse the arguments
     */
    @Override
    public List<CommandArgument> parseArguments(String token) throws Exception {
        String sPattern = getStaticFieldValue("PATTERN");
        if (!isParseable(token)) {
            throw new Exception("Cannot parse token '" + token + "' because it doesn't match the pattern '" + sPattern + "'. See class: " + getClass().getCanonicalName());
        }
        List<CommandArgument> ret = new ArrayList<>();
        if (token != null) {
            Pattern pattern = Pattern.compile(sPattern);
            Matcher matcher = pattern.matcher(token);
            if (matcher.matches()) {
                if (matcher.groupCount() == 0) {
                    if (token.contains(" ") && token.endsWith(";")) {
                        int ix = token.indexOf(" ");
                        String s = token.substring(ix, token.length() - 1).trim();
                        CommandArgument commandArgument = getSingleParameter(s);
                        if (commandArgument != null) {
                            ret.add(commandArgument);
                        }
                    }
                } else {
                    List<CommandArgument> args = getFromMultipleArguments(matcher);
                    if (args.size() > 0) {
                        ret.addAll(args);
                    }
                }
            }
        }
        return ret;
    }

    protected CommandArgument getSingleParameter(String s) {
        return null;
    }

    protected List<CommandArgument> getFromMultipleArguments(Matcher matcher) {
        List<CommandArgument> ret = new ArrayList<>();
        int max = matcher.groupCount();
        String matchFound = "";
        int pos = 0;
        for (int i = 0; i < max; i++) {
            String s = matcher.group(i);
            if (!(StringUtils.isBlank(s) || s.matches(getSpecialCharactersToAvoid()))) {
                if (matchFound.length() > s.length()) {
                    int ix = matchFound.indexOf(s);
                    if (ix == pos) {
                        pos++;
                        continue;
                    }
                }
                CommandArgument commandArgument = getCommandArgument(ret, s, i);
                if (commandArgument != null) {
                    //logger.debug("Add argument: " + commandArgument.toString());
                    ret.add(commandArgument);
                    matchFound = commandArgument.toString();
                    pos = 2;
                }
            }
        }
        return ret;
    }

    protected abstract CommandArgument getCommandArgument(List<CommandArgument> arguments, String token, int i);

    protected String getSpecialCharactersToAvoid() {
        return CHARACTERS_TO_AVOID;
    }

    protected String getStaticFieldValue(String name) {
        String ret = null;
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                if (StringUtils.isNotBlank(field.getName()) && field.getName().equals(name)) {
                    ret = field.get(null).toString();
                }
            }
        } catch (Exception e) {
            logger.error("The static field {} cannot be resolved for class {}", name, getClass().getName());
        }
        return ret;
    }

    protected void validateArguments(List<CommandArgument> arguments, int size) throws Exception {
        argumentValidator.validateArguments(arguments, size);
    }

    protected <T> void validateArguments(List<CommandArgument> arguments, Class<T> clazz, int size) throws Exception {
        argumentValidator.validateArguments(arguments, clazz, null, size);
    }

    protected <T1, T2> void validateArguments(List<CommandArgument> arguments, Class<T1> clazz1, Class<T2> clazz2, int size) throws Exception {
        argumentValidator.validateArguments(arguments, clazz1, clazz2, size);
    }

    protected <T> void validate(CommandArgument arg, Class<T> clazz) throws Exception {
        argumentValidator.validate(arg, clazz, null);
    }

    protected <T1, T2> void validate(CommandArgument arg, Class<T1> clazz1, Class<T2> clazz2) throws Exception {
        argumentValidator.validate(arg, clazz1, clazz2);
    }

    protected FormaterCfg getFormaterCfg(String name, Locale locale) {
        FormaterCfg ret = null;
        try {
            Description description = ResourceUtils.getResources("/description", locale, Description.class);
            if (((ret = findFormaterCfg(description.getMode1(), name)) == null) &&
                    ((ret = findFormaterCfg(description.getMode2(), name)) == null) &&
                    ((ret = findFormaterCfg(description.getModec(), name)) == null)) {
                logger.error("Could not locate a formater configuration for name {}", name);
            }
        } catch (Exception e) {
            logger.error("Cannot read description from configuration for locale", e);
        }
        return ret;
    }

    private FormaterCfg findFormaterCfg(List<FormaterCfg> formaterCfgs, String name) {
        FormaterCfg ret = null;
        for (FormaterCfg formaterCfg : formaterCfgs) {
            if (formaterCfg.getName().equals(name)) {
                ret = formaterCfg;
                break;
            }
        }
        return ret;
    }

    /**
     * Given a string containing multiple comma characters, this reduces to output to the part that contains only the
     * maximum number
     *
     * @param s           the string to reduce
     * @param maxNrCommas the maximum number of comma signs
     * @return the result of trimming
     */
    protected String trimString(String s, int maxNrCommas) {
        String ret = s;
        for (int j = 0, nr = 0; j < s.length(); j++) {
            int k = s.indexOf(",", j);
            if (k > j) {
                nr++;
                j = k;
                if (nr > maxNrCommas) {
                    ret = s.substring(0, j);
                    break;
                }
            }
        }
        return ret;
    }
}
