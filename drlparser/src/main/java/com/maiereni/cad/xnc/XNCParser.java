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
package com.maiereni.cad.xnc;

import com.maiereni.cad.xnc.bo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser of XNC Content
 *
 * @author Petre Maierean
 */
public class XNCParser {
    private static final Logger logger = LogManager.getLogger(XNCParser.class);
    private static final List<Tokens> TOKENS = initTokens();
    private Float offsetX, offsetY;

    /**
     * Read the content of a string
     *
     * @param s
     * @return
     * @throws Exception
     */
    public List<Token> parse(String s) throws Exception {
        if (StringUtils.isBlank(s)) {
            throw new Exception("The argument is blank or null");
        }
        try (StringReader sr = new StringReader(s)) {
            return parse(sr);
        }
    }

    /**
     * Parse the content of a XNC file
     *
     * @param f
     * @return
     * @throws Exception
     */
    public List<Token> parse(File f) throws Exception {
        if (f == null) {
            throw new Exception("The argument cannot be null");
        }
        if (!f.isFile()) {
            throw new Exception("No file can be found at " + f.getPath());
        }
        try (FileReader fr = new FileReader(f)) {
            return parse(fr);
        }
    }

    /**
     * Read the content of a reader
     *
     * @param reader
     * @return
     * @throws Exception
     */
    public List<Token> parse(Reader reader) throws Exception {
        List<Token> ret = null;
        try (LineNumberReader lnr = new LineNumberReader(reader)) {
            ret = new ArrayList<>();
            String s = null;
            for (int lineNumber = 1; (s = lnr.readLine()) != null; lineNumber++) {
                if (s.startsWith(";")) {
                    continue;
                }
                Token token = parseToken(s);
                if (token == null) {
                    throw new Exception("Cannot interpret token '" + s + "' at line " + lineNumber);
                }
                ret.add(token);
            }
        }
        return ret;
    }

    private Token parseToken(String s) throws Exception {
        Token ret = null;
        Tokens token = match(s);
        if (token != null) {
            switch (token) {
                case drill:
                    ret = parseDrillHole(s);
                    break;
                case move:
                    ret = parseMove(s);
                    break;
                case select:
                    ret = parseSelect(s);
                    break;
                case tool:
                    ret = parseTool(s);
                    break;
                case format:
                    ret = parseFormat(s);
                    break;
                case setDrillMode:
                    ret = parseDrillMode(s);
                    break;
                default:
                    ret = token.getInstance();
            }
        }
        return ret;
    }

    private DrillHole parseDrillHole(String s) {
        DrillHole ret = null;
        Pattern pattern = Pattern.compile(DrillHole.PATTERN);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            try {
                float x = Float.parseFloat(matcher.group(1));
                float y = Float.parseFloat(matcher.group(5));
                x += offsetX;
                y += offsetY;
                ret = new DrillHole(x, y);
            } catch (Exception e) {

            }
        }
        return ret;
    }

    private Move parseMove(String s) {
        Move ret = null;
        Pattern pattern = Pattern.compile(Move.PATTERN);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            try {
                float x = Float.parseFloat(matcher.group(1));
                float y = Float.parseFloat(matcher.group(5));
                x += offsetX;
                y += offsetY;
                ret = new Move(x, y);
            } catch (Exception e) {

            }
        }
        return ret;
    }

    private SelectTool parseSelect(String s) {
        SelectTool ret = null;
        Pattern pattern = Pattern.compile(SelectTool.PATTERN);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            ret = new SelectTool(s.substring(1));
        }
        return ret;
    }

    private Tool parseTool(String s) {
        Tool ret = null;
        Pattern pattern = Pattern.compile(Tool.PATTERN);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            try {
                String id = matcher.group(1);
                float diameter = Float.parseFloat(matcher.group(2));
                ret = new Tool(id, diameter);
            } catch (Exception e) {

            }
        }
        return ret;
    }

    private Format parseFormat(String s) {
        Format ret = null;
        Pattern pattern = Pattern.compile(Format.PATTERN);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            ret = new Format(s.substring(4));
        }

        return ret;
    }

    private SetDrillMode parseDrillMode(String s) {
        SetDrillMode ret = null;
        Pattern pattern = Pattern.compile(SetDrillMode.PATTERN);
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            ret = new SetDrillMode(s.substring(1));
        }

        return ret;
    }

    private Tokens match(String s) {
        Tokens ret = null;
        for (Tokens token : TOKENS) {
            if (s.matches(token.getPattern())) {
                ret = token;
                break;
            }
        }
        return ret;
    }

    private static List<Tokens> initTokens() {
        List<Tokens> ret = new ArrayList<>();
        for (Tokens token : Tokens.values()) {
            ret.add(token);
        }
        return ret;
    }

    public Float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Float offsetX) {
        this.offsetX = offsetX;
    }

    public Float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Float offsetY) {
        this.offsetY = offsetY;
    }
}
