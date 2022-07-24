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
import com.maiereni.cad.rml1.RML1CommandFormat;
import com.maiereni.cad.rml1.bo.*;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

/**
 * Utility class to parse a string into various command arguments
 *
 * @author Petre Maierean
 */
public class ArgumentParser {

    /**
     * Parse a string to a command argument
     *
     * @param s
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T extends CommandArgument> T parse(String s, Class<T> clazz) throws Exception {
        T ret = null;
        if (StringUtils.isNotBlank(s) || clazz == null) {
            if (clazz.equals(IntArgument.class)) {
                ret = (T) parseInteger(s);
            } else if (clazz.equals(LongArgument.class)) {
                ret = (T) parseLong(s);
            } else if (clazz.equals(FloatArgument.class)) {
                ret = (T) parseFloat(s);
            } else if (clazz.equals(PairOfFloatsArgument.class)) {
                ret = (T) parsePairOfFloats(s);
            } else if (clazz.equals(VertexArgument.class)) {
                ret = (T) parseVertex(s);
            } else if (clazz.equals(AxisArgument.class)) {
                ret = (T) parseAxis(s);
            } else if (clazz.equals(CallArgument.class)) {
                ret = (T) parseCallArgument(s);
            }
        } else {
            throw new Exception("The arguments cannot be null");
        }
        return ret;
    }

    private IntArgument parseInteger(String s) throws Exception {
        IntArgument ret = null;
        if (StringUtils.isNotBlank(s)) {
            int i = Integer.parseInt(s.trim());
            ret = new IntArgument(i);
        }
        return ret;
    }

    private LongArgument parseLong(String s) throws Exception {
        LongArgument ret = null;
        if (StringUtils.isNotBlank(s)) {
            long i = Long.parseLong(s.trim());
            ret = new LongArgument(i);
        }
        return ret;
    }

    private FloatArgument parseFloat(String s) throws Exception {
        FloatArgument ret = null;
        if (StringUtils.isNotBlank(s)) {
            float f = Float.parseFloat(s.trim());
            ret = new FloatArgument(f);
        }
        return ret;
    }

    private PairOfFloatsArgument parsePairOfFloats(String s) throws Exception {
        PairOfFloatsArgument ret = null;
        if (StringUtils.isNotBlank(s)) {
            String tok = s;
            Matcher matcher = RML1CommandFormat.MORE.matcher(s);
            if (matcher.matches()) {
                int i = tok.indexOf(",");
                int j = tok.indexOf(",", i + 1);
                if (j > 0) {
                    tok = tok.substring(0, j);
                }
            }
            if (tok.matches(RML1CommandFormat.PAIR_OF_FLOATS_PATTERN)) {
                String[] toks = s.split(",");
                ret = new PairOfFloatsArgument();
                ret.setX(Float.parseFloat(toks[0].trim()));
                ret.setY(Float.parseFloat(toks[1].trim()));
            }
        } else {
            throw new Exception("A blank string as argument");
        }
        return ret;
    }

    private VertexArgument parseVertex(String s) throws Exception {
        VertexArgument ret = null;
        if (StringUtils.isNotBlank(s)) {
            if (s.matches(RML1CommandFormat.THREE_FLOATS_PATTERN)) {
                String[] toks = s.split(",");
                ret = new VertexArgument();
                ret.setX(Float.parseFloat(toks[0]));
                ret.setY(Float.parseFloat(toks[1]));
                ret.setZ(Float.parseFloat(toks[2]));
            }
        } else {
            throw new Exception("A blank string as argument");
        }
        return ret;
    }

    private AxisArgument parseAxis(String s) throws Exception {
        AxisArgument ret = null;
        if (StringUtils.isNotBlank(s)) {
            Float a = extractFloat(s, RML1CommandFormat.ANY_A_PATTERN),
                    x = extractFloat(s, RML1CommandFormat.ANY_X_PATTERN),
                    y = extractFloat(s, RML1CommandFormat.ANY_Y_PATTERN),
                    z = extractFloat(s, RML1CommandFormat.ANY_Z_PATTERN);
            if (!(a == null && x == null && y == null && z == null)) {
                ret = new AxisArgument(x, y, z, a);
            }
        } else {
            throw new Exception("A blank string as argument");
        }
        return ret;
    }

    private Float extractFloat(String s, String pattern) {
        Float ret = null;
        if (s.matches(pattern)) {
            try {
                String f = s.substring(1);
                ret = Float.parseFloat(f);
            } catch (Exception e) {
            }
        }
        return ret;
    }

    private CallArgument parseCallArgument(String s) {
        CallArgument ret = null;
        for (Mode2Command mc : Mode2Command.values()) {
            if (mc.getCommandLetter().equals(s)) {
                ret = new CallArgument(mc);
                break;
            }
        }
        return ret;
    }
}
