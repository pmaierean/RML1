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

import com.maiereni.cad.xnc.bo.DrillHole;
import com.maiereni.cad.xnc.bo.Move;
import com.maiereni.cad.xnc.bo.SelectTool;
import com.maiereni.cad.xnc.bo.Tool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for the XNCParser
 *
 * @author Petre Maierean
 */
public class XNCParserTest {
    private static final Logger logger = LogManager.getLogger(XNCParser.class);
    private XNCParser parser = new XNCParser();

    @Test
    public void testParseDrillHoleSmall() {
        try {
            List<Token> tokens = parser.parse("X1.504Y12.553");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof DrillHole);
            DrillHole drillHole = (DrillHole) tokens.get(0);
            assertTrue(drillHole.getX() == 1.504F);
            assertTrue(drillHole.getY() == 12.553F);
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseDrillLarge() {
        try {
            List<Token> tokens = parser.parse("X123.4Y234.5");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof DrillHole);
            DrillHole drillHole = (DrillHole) tokens.get(0);
            assertTrue(drillHole.getX() == 123.4F);
            assertTrue(drillHole.getY() == 234.5F);
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseNegativeXDrill() {
        try {
            List<Token> tokens = parser.parse("X-123.4Y234.5");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof DrillHole);
            DrillHole drillHole = (DrillHole) tokens.get(0);
            assertTrue(drillHole.getX() == -123.4F);
            assertTrue(drillHole.getY() == 234.5F);
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseNegativeYDrill() {
        try {
            List<Token> tokens = parser.parse("X123.4Y-234.5");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof DrillHole);
            DrillHole drillHole = (DrillHole) tokens.get(0);
            assertTrue(drillHole.getX() == 123.4F);
            assertTrue(drillHole.getY() == -234.5F);
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseMoveSmall() {
        try {
            List<Token> tokens = parser.parse("G00X1.504Y12.553");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof Move);
            Move move = (Move) tokens.get(0);
            assertTrue(move.getX() == 1.504F);
            assertTrue(move.getY() == 12.553F);
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseMoveLarge() {
        try {
            List<Token> tokens = parser.parse("G00X123.4Y234.5");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof Move);
            Move move = (Move) tokens.get(0);
            assertTrue(move.getX() == 123.4F);
            assertTrue(move.getY() == 234.5F);
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseSelectTool() {
        try {
            List<Token> tokens = parser.parse("T01");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof SelectTool);
            SelectTool select = (SelectTool) tokens.get(0);
            assertEquals("Expected tool", "01", select.getId());
        } catch (Exception e) {
            logger.error("Failed to parse a drill string", e);
            fail();
        }
    }

    @Test
    public void testParseTool() {
        assertTrue("T01C123.4".matches(Tool.PATTERN));
        try {
            List<Token> tokens = parser.parse("T01C123.4");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof Tool);
            Tool tool = (Tool) tokens.get(0);
            assertEquals("Expected tool", "01", tool.getId());
            assertTrue(tool.getDiameter() == 123.4F);
        } catch (Exception e) {
            logger.error("Failed to parse a tool string", e);
            fail();
        }
    }

    @Test
    public void testParseToolL() {
        try {
            List<Token> tokens = parser.parse("T01C1.521");
            assertNotNull(tokens);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.get(0) instanceof Tool);
            Tool tool = (Tool) tokens.get(0);
            assertEquals("Expected tool", "01", tool.getId());
            assertTrue(tool.getDiameter() == 1.521F);
        } catch (Exception e) {
            logger.error("Failed to parse a tool string", e);
            fail();
        }
    }

    private static final String SAMPLE = "M48\n" +
            "; DRILL file {KiCad (6.0.5)} date Fri Jun 17 07:00:03 2022\n" +
            "; FORMAT={-:-/ absolute / metric / decimal}\n" +
            "; #@! TF.CreationDate,2022-06-17T07:00:03-04:00\n" +
            "; #@! TF.GenerationSoftware,Kicad,Pcbnew,(6.0.5)\n" +
            "; #@! TF.FileFunction,Plated,1,2,PTH\n" +
            "FMAT,2\n" +
            "METRIC\n" +
            "; #@! TA.AperFunction,Plated,PTH,ViaDrill\n" +
            "T1C0.400\n" +
            "; #@! TA.AperFunction,Plated,PTH,ComponentDrill\n" +
            "T2C0.800\n" +
            "; #@! TA.AperFunction,Plated,PTH,ComponentDrill\n" +
            "T3C1.100\n" +
            "; #@! TA.AperFunction,Plated,PTH,ComponentDrill\n" +
            "T4C1.300\n" +
            "; #@! TA.AperFunction,Plated,PTH,ComponentDrill\n" +
            "T5C1.600\n" +
            "; #@! TA.AperFunction,Plated,PTH,ViaDrill\n" +
            "T6C3.000\n" +
            "%\n" +
            "G90\n" +
            "G05\n" +
            "T1\n" +
            "X35.56Y-40.64\n" +
            "X40.64Y-55.88\n" +
            "X43.18Y-73.66\n" +
            "X48.26Y-40.64\n" +
            "X48.26Y-73.66\n" +
            "X53.34Y-55.88\n" +
            "X53.34Y-73.66\n" +
            "T2\n" +
            "X48.07Y-58.42\n" +
            "X48.07Y-63.5\n" +
            "X52.4Y-48.26\n" +
            "X52.4Y-53.34\n" +
            "X53.135Y-36.84\n" +
            "X53.135Y-44.46\n" +
            "X53.15Y-71.12\n" +
            "X54.43Y-58.42\n" +
            "X54.43Y-60.96\n" +
            "X54.43Y-63.5\n" +
            "X54.43Y-66.04\n" +
            "X55.3Y-48.26\n" +
            "X55.3Y-53.34\n" +
            "X55.675Y-36.84\n" +
            "X55.675Y-44.22\n" +
            "T0\n" +
            "M30";

    @Test
    public void testParseLargeSample() {
        try {
            List<Token> tokens = parser.parse(SAMPLE);
            assertNotNull(tokens);
            assertTrue(tokens.size() == 38);
        } catch (Exception e) {
            logger.error("Failed to parse a larger string", e);
            e.printStackTrace();
            fail();
        }
    }
}

