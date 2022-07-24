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
package com.maiereni.cad.toolPath;

import com.maiereni.cad.rml1.bo.ComplexArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;
import com.maiereni.cad.rml1.complex.MoveAndDrill;
import com.maiereni.cad.toolPath.bo.RoutingArguments;
import com.maiereni.cad.toolPath.bo.RoutingPath;
import com.maiereni.cad.xnc.RN1PrinterStatus;
import com.maiereni.cad.xnc.RN1PrinterStatusFactory;
import com.maiereni.cad.xnc.Token;
import com.maiereni.cad.xnc.XNCParser;
import com.maiereni.cad.xnc.bo.DrillHole;
import com.maiereni.cad.xnc.bo.SelectTool;
import com.maiereni.cad.xnc.bo.Tool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * A class that converts an XNC drill file to instructions to drill in RNL-1
 *
 * @author Petre Maierean
 */
public class ToolPathGenerator {
    private static final Logger logger = LogManager.getLogger(ToolPathGenerator.class);

    /**
     * Generate toolpath from dlr string
     *
     * @param drlString
     * @param arguments the routing arguments
     * @return
     * @throws Exception
     */
    public Map<String, String> generateToolpathfromDrl(String drlString, RoutingArguments arguments) throws Exception {
        if (StringUtils.isBlank(drlString)) {
            throw new Exception("The argument is blank or null");
        }
        try (StringReader sr = new StringReader((drlString))) {
            return generateToolpathfromDrl(sr, arguments);
        }
    }

    /**
     * Generate toolpath from dlr file
     *
     * @param drlFile
     * @param arguments the routing arguments
     * @return
     * @throws Exception
     */
    public Map<String, String> generateToolpathfromDrl(File drlFile, RoutingArguments arguments) throws Exception {
        if (drlFile == null) {
            throw new Exception("The argument is null");
        }
        if (!drlFile.isFile()) {
            throw new Exception("Cannot file DRL file at " + drlFile.getPath());
        }
        try (FileReader reader = new FileReader(drlFile)) {
            return generateToolpathfromDrl(reader, arguments);
        }
    }

    /**
     * Generate toolpath from dlr drl reader
     *
     * @param drlReader
     * @param arguments the routing arguments
     * @return
     * @throws Exception
     */
    public Map<String, String> generateToolpathfromDrl(Reader drlReader, RoutingArguments arguments) throws Exception {
        if (drlReader == null || arguments == null) {
            throw new Exception("Neither argument can be null");
        }
        XNCParser parser = new XNCParser();
        parser.setOffsetX(arguments.getOffsetX());
        parser.setOffsetY(arguments.getOffsetY());
        List<Token> tokens = parser.parse(drlReader);
        return generateToolpath(tokens, arguments);
    }

    /**
     * Generate toolpath for a set of tokens. Use the Z0 and Z1 position for drilling positions
     *
     * @param tokens
     * @param arguments the routing arguments
     * @return
     * @throws Exception
     */
    public Map<String, String> generateToolpath(List<Token> tokens, RoutingArguments arguments) throws Exception {
        if (tokens == null || arguments == null) {
            throw new Exception("Neither argument can be null");
        }
        Map<String, String> ret = new LinkedHashMap<String, String>();
        RN1PrinterStatus status = RN1PrinterStatusFactory.get().getInitialized(tokens);
        logger.debug("Initial definitions: {}", status.toString());
        if (status.getTools() != null) {
            for (Tool tool : status.getTools()) {
                RoutingPath routingPath = getRoutingPath(tool, tokens);
                ret.put(tool.toString(), generateToolpath(routingPath, arguments));
                logger.debug("Generated the toolpath for tool {}", tool.getId());
            }
        }
        return ret;
    }

    private String generateToolpath(RoutingPath routingPath, RoutingArguments arguments) throws Exception {
        String ret = null;
        if (arguments.getForStepping() > 0) {
            ret = generateToolpathSteps(routingPath, arguments);
        } else {
            ret = generateToolpathFull(routingPath, arguments);
        }
        return ret;
    }

    private String generateToolpathSteps(RoutingPath routingPath, RoutingArguments arguments) throws Exception {
        MoveAndDrill generator = new MoveAndDrill();
        ComplexArgument complexArgument = makeComplexArgument(arguments);
        StringWriter sw = new StringWriter();
        float maxX = 0, maxY = 0, maxZ = 0;
        if (routingPath.getDrillHoles() != null) {
            int nr = 0;
            for (DrillHole drillHole : routingPath.getDrillHoles()) {
                nr++;
                VertexArgument vertexArgument = createVertex(drillHole, arguments);
                if (vertexArgument.getX() > maxX) {
                    maxX = vertexArgument.getX();
                }
                if (vertexArgument.getY() > maxY) {
                    maxY = vertexArgument.getY();
                }
                if (vertexArgument.getZ() < maxZ) {
                    maxZ = vertexArgument.getZ();
                }
                complexArgument.getPoints().add(vertexArgument);
                if (nr == arguments.getForStepping()) {
                    sw.write(generator.generateCommand(complexArgument));
                    sw.write("\r\n");
                    complexArgument.getPoints().clear();
                    nr = 0;
                }
            }
            if (nr > 0) {
                sw.write(generator.generateCommand(complexArgument));
                sw.write("\r\n");
                complexArgument.getPoints().clear();
            }
        }
        String ret = sw.toString();
        if (arguments.isWriteExtremes()) {
            complexArgument.getPoints().clear();
            VertexArgument vertexArgument = new VertexArgument();
            vertexArgument.setX(0f);
            vertexArgument.setY(0f);
            vertexArgument.setZ(maxZ);
            complexArgument.getPoints().add(vertexArgument);
            vertexArgument = new VertexArgument();
            vertexArgument.setX(maxX);
            vertexArgument.setY(maxY);
            vertexArgument.setZ(maxZ);
            complexArgument.getPoints().add(vertexArgument);
            String header = generator.generateCommand(complexArgument);
            ret = header + "\r\n" + ret;
        }
        return ret;
    }

    private String generateToolpathFull(RoutingPath routingPath, RoutingArguments arguments) throws Exception {
        MoveAndDrill generator = new MoveAndDrill();
        String ret = null;
        ComplexArgument complexArgument = makeComplexArgument(arguments);
        complexArgument.setReset(true);

        if (routingPath.getDrillHoles() != null) {
            for (DrillHole drillHole : routingPath.getDrillHoles()) {
                VertexArgument vertexArgument = createVertex(drillHole, arguments);
                complexArgument.getPoints().add(vertexArgument);
            }
        }
        ret = generator.generateCommand(complexArgument);
        ret = ret.replaceAll("\\x3b", "\n");
        return ret;
    }

    private VertexArgument createVertex(DrillHole drillHole, RoutingArguments arguments) {
        float x = drillHole.getX() / arguments.getUnitConversionRate();
        float y = drillHole.getY() / arguments.getUnitConversionRate();
        float z = arguments.getZ1() / arguments.getUnitConversionRate();
        return new VertexArgument(x, y, z);
    }

    private ComplexArgument makeComplexArgument(RoutingArguments arguments) {
        ComplexArgument complexArgument = new ComplexArgument();
        complexArgument.setZ(arguments.getZ0() / arguments.getUnitConversionRate());
        complexArgument.setSpeedXY(15f);
        if (arguments.getSpeedZ() != null) {
            complexArgument.setSpeedZ(arguments.getSpeedZ());
        }
        List<VertexArgument> points = new ArrayList<>();
        complexArgument.setPoints(points);
        return complexArgument;
    }

    private RoutingPath getRoutingPath(Tool tool, List<Token> tokens) throws Exception {
        RoutingPath ret = new RoutingPath();
        ret.setTool(tool);
        ret.setDrillHoles(getDrillHoles(tool, tokens));
        return ret;
    }

    private List<DrillHole> getDrillHoles(Tool tool, List<Token> tokens) throws Exception {
        List<DrillHole> ret = new ArrayList<>();
        boolean take = false;
        for (Token token : tokens) {
            if (token instanceof DrillHole) {
                DrillHole drillHole = (DrillHole) token;
                if (take) {
                    ret.add(drillHole);
                }
            } else if (token instanceof SelectTool) {
                SelectTool t = (SelectTool) token;
                if (t.getId().equals(tool.getId())) {
                    take = true;
                } else {
                    take = false;
                }
            } else {
                take = false;
            }
        }

        ret.sort(new Comparator<DrillHole>() {
            @Override
            public int compare(DrillHole o1, DrillHole o2) {
                int ret = 0;
                if (o1.getY() > o2.getY()) {
                    ret = 1;
                } else {
                    ret = -1;
                }
                return ret;
            }
        });

        return ret;
    }
}
