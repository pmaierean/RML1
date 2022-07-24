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

import com.maiereni.cad.rml1.bo.ComplexArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;
import com.maiereni.cad.rml1.complex.MoveAndDrill;
import com.maiereni.cad.rml1.complex.PenMovement;
import com.maiereni.cad.rml1.util.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Petre Maierean
 */
public class RML1CommandExecutorTest {
    private static final Logger logger = LogManager.getLogger(RML1CommandExecutorTest.class);
    private RML1CommandExecutor executor;
    private Translator translator = new Translator();

    @Before
    public void setUp() {
        executor = new RML1CommandExecutor();
        executor.setPrintJobListener(new TestPrinterJobListener());
    }

    private static final String MULTIPLE_COMMANDS =
            "^IN\n" +
                    "V15\n" +
                    "!VZ2\n" +
                    "!MC1\n" +
                    "M64,64\n" +
                    "!ZM-100\n" +
                    "!ZM0\n" +
                    "M4064,64\n" +
                    "!ZM-100\n" +
                    "!ZM0\n" +
                    "H\n" +
                    "!MC0\n";

    @Test
    public void testPrintDownCommands() {
        try {
            String drillPlan = generateTestPenMovement(5);//generateTestDrillCommand2(5);
            logger.debug("Print\r\n" + drillPlan);
            logger.debug("Send commands:\r\n{}", translator.translate(drillPlan, null));
            executor.sendCommandString(drillPlan);
            assertTrue(true);
        } catch (Exception e) {
            logger.error("Failed to send home movement command", e);
            fail();
        }
    }

    public String generateTestPenMovement(int number) throws Exception {
        ComplexArgument complexArgument = new ComplexArgument();
        complexArgument.setReset(true);
        complexArgument.setZ(0f);
        List<VertexArgument> points = new ArrayList<>();
        float z = -2000f;
        Random rd = new Random();
        for (int i = 0; i < number; i++) {
            Float x = rd.nextFloat(0, 5500f);
            Float y = rd.nextFloat(0, 3000f);
            points.add(new VertexArgument(x, y, z));
        }
        complexArgument.setPoints(points);
        complexArgument.setSpeedXY(15f);
        String s = new PenMovement().generateCommand(complexArgument);
        s = s.replaceAll("\\x3B", "\n\r");
        return s;
    }

    public String generateTestDrillCommand2(int number) throws Exception {
        ComplexArgument complexArgument = new ComplexArgument();
        complexArgument.setReset(true);
        complexArgument.setZ(0f);
        List<VertexArgument> points = new ArrayList<>();
        float z = -2000f;
        Random rd = new Random();
        for (int i = 0; i < number; i++) {
            Float x = rd.nextFloat(0, 6000f);
            Float y = rd.nextFloat(0, 3000f);
            points.add(new VertexArgument(x, y, z));
        }
        complexArgument.setPoints(points);
        complexArgument.setSpeedXY(15f);
        //complexArgument.setSpeedZ(2f);
        String s = new MoveAndDrill().generateCommand(complexArgument);
        s = s.replaceAll("\\x3B", "\n\r");
        return s;
    }

    public String generateTestDrillCommand1() throws Exception {
        ComplexArgument complexArgument = new ComplexArgument();
        complexArgument.setReset(true);
        complexArgument.setZ(0f);
        List<VertexArgument> points = new ArrayList<>();
        float z = -2000f;
        points.add(new VertexArgument(1440.8f, 1646.7f, z));
        points.add(new VertexArgument(1646.7f, 2264.2f, z));
        points.add(new VertexArgument(1749.6f, 2984.6f, z));
        complexArgument.setPoints(points);
        complexArgument.setSpeedXY(15f);
        //complexArgument.setSpeedZ(2f);
        String s = new MoveAndDrill().generateCommand(complexArgument);
        s = s.replaceAll("\\x3B", "\n");
        return s;
    }


    /**
     * Sends a command file. Accepted arguments:
     *
     * @param args: file
     * @param args
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new Exception("Null argument");
            }
            File fPrnt = new File(args[0]);
            RML1CommandExecutor executor = new RML1CommandExecutor();
            executor.setPrintJobListener(new TestPrinterJobListener());
            boolean stepByStep = false;
            if (System.getProperty("step_by_step") != null) {
                stepByStep = true;
                try (FileReader fr = new FileReader(fPrnt);
                     LineNumberReader lnr = new LineNumberReader(fr)) {
                    int line = 1;
                    String s = null;
                    while ((s = lnr.readLine()) != null) {
                        logger.debug("Send line: {} {}", line, s);
                        executor.sendCommandString(s);
                        logger.debug("Done sending. Next? (Y/n)");
                        byte[] buffer = new byte[10];
                        int count = System.in.read(buffer);
                        logger.debug("Received: " + buffer[0]);
                        if (!(buffer[0] == 89 || buffer[0] == 121)) {
                            break;
                        }
                        line++;
                    }
                }
            } else {
                executor.sendCommandFile(fPrnt);
            }
            logger.debug("Done");
        } catch (Exception e) {
            logger.error("Failed to send file to execute", e);
        }
    }


    public static class TestPrinterJobListener implements PrintJobListener {
        @Override
        public void printDataTransferCompleted(PrintJobEvent pje) {
            logger.error("Data transfer complete");
        }

        @Override
        public void printJobCompleted(PrintJobEvent pje) {
            logger.error("Print Job completed");
        }

        @Override
        public void printJobFailed(PrintJobEvent pje) {
            logger.error("Print Job failed");
        }

        @Override
        public void printJobCanceled(PrintJobEvent pje) {
            logger.error("Print Job canceled");
        }

        @Override
        public void printJobNoMoreEvents(PrintJobEvent pje) {
            logger.error("No more events");
        }

        @Override
        public void printJobRequiresAttention(PrintJobEvent pje) {
            logger.error("Print Job requeres attention");
        }
    }
}
