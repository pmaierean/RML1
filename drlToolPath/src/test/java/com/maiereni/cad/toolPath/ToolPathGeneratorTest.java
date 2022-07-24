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

import com.maiereni.cad.toolPath.bo.RoutingArguments;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Petre Maierean
 */
public class ToolPathGeneratorTest {
    private static final Logger logger = LogManager.getLogger(ToolPathGeneratorTest.class);

    /**
     * Generate tool path for a file
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Arguments arguments = new Arguments(args);
            if (StringUtils.isNotBlank(arguments.sFile)) {
                ToolPathGenerator toolPathGenerator = new ToolPathGenerator();
                File f = new File(arguments.sFile);
                Map<String, String> r = toolPathGenerator.generateToolpathfromDrl(f, arguments.routingArguments);
                logger.debug("Done generating tool path");
                if (StringUtils.isNotBlank(arguments.out)) {
                    File fDir = new File((arguments.out));
                    if (!fDir.exists()) {
                        if (!fDir.mkdirs()) {
                            throw new Exception("Could not make directory");
                        }
                    }
                    StringWriter tools = new StringWriter();
                    for (Map.Entry<String, String> entry : r.entrySet()) {
                        String key = entry.getKey();
                        int ix = key.indexOf(" ");
                        String fName = "rnl1-" + key.substring(0, ix) + ".out";
                        tools.write(key);
                        tools.write("=");
                        tools.write(fName);
                        tools.write("\r\n");
                        File fOut = new File(fDir, fName);
                        FileUtils.write(fOut, entry.getValue(), Charset.defaultCharset());
                        logger.debug("Wrote file {} for the tool {}", fOut.getPath(), entry.getKey());
                    }
                    FileUtils.write(new File(fDir, "Instructions.txt"), tools.toString(), Charset.defaultCharset());
                }
            }

        } catch (Exception e) {
            logger.error("Failed to generate", e);
        }
    }

    private static class Arguments {
        RoutingArguments routingArguments = new RoutingArguments();
        String sFile = null, out = null;

        public Arguments(String[] args) {
            routingArguments.setUnitConversionRate(1);
            routingArguments.setWriteExtremes(true);
            if (args.length > 1) {
                for (String arg : args) {
                    int ix = arg.indexOf("=");
                    if (ix > 0) {
                        String val = arg.substring(ix + 1);
                        if (arg.startsWith("-z0=")) {
                            routingArguments.setZ0(Float.parseFloat(val));
                        } else if (arg.startsWith("-z1=")) {
                            routingArguments.setZ1(Float.parseFloat(val));
                        } else if (arg.startsWith("-out=")) {
                            out = val;
                        } else if (arg.startsWith("-forStepping=")) {
                            routingArguments.setForStepping(Integer.parseInt(val));
                        } else if (arg.startsWith("-offsetX=")) {
                            routingArguments.setOffsetX(Float.parseFloat(val));
                        } else if (arg.startsWith("-offsetY=")) {
                            routingArguments.setOffsetY(Float.parseFloat(val));
                        } else if (arg.startsWith("-unitConversion=")) {
                            routingArguments.setUnitConversionRate(Float.parseFloat(val));
                        }

                    } else {
                        sFile = arg;
                    }
                }
            }
        }
    }
}
