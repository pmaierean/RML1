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
package com.maiereni.cad.rml1.complex;

import com.maiereni.cad.rml1.bo.ComplexArgument;
import com.maiereni.cad.rml1.bo.VertexArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Petre Maierean
 */
public class PenMovementTest {
    private static final Logger logger = LogManager.getLogger(PenMovementTest.class);
    private PenMovement generator = new PenMovement();

    @Test
    public void testNull() {
        try {
            generator.generateCommand(null);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBlank() {
        try {
            ComplexArgument arg = new ComplexArgument();
            generator.generateCommand(arg);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSimplePath() {
        try {
            ComplexArgument arg = new ComplexArgument();
            arg.setPoints(new ArrayList<>());
            arg.getPoints().add(new VertexArgument(123.0f, 234.1f, -200.1f));
            String s = generator.generateCommand(arg);
            assertEquals("The command expected", "^IN;@ -200,0;^ PU 123,234.1;^ PD;^ PU;!MC 0;", s);
        } catch (Exception e) {
            logger.error("Failed to create commands", e);
            fail();
        }
    }

    @Test
    public void testManyPath() {
        try {
            ComplexArgument arg = new ComplexArgument();
            arg.setZ(0f);
            arg.setPoints(getTestVertexArgument());
            String s = generator.generateCommand(arg);
            assertNotNull(s);
            logger.debug("Command is:\r\n{}", s);
        } catch (Exception e) {
            logger.error("Failed to create commands", e);
            fail();
        }
    }


    private List<VertexArgument> getTestVertexArgument() {
        List<VertexArgument> ret = new ArrayList<>();
        float crtX = 10, crtY = 10;
        for (int i = 0; i < 10; i++) {
            crtX += i * 10f;
            crtY += i * 10f;
            VertexArgument vertexArgument = new VertexArgument(crtX, crtY, -2000f);
            ret.add(vertexArgument);
        }
        return ret;
    }

}
