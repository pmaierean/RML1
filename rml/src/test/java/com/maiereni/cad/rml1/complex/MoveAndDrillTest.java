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
 * Unit test for the Move and Drill
 *
 * @author Petre Maierean
 */
public class MoveAndDrillTest {
    private static final Logger logger = LogManager.getLogger(MoveAndDrillTest.class);
    private MoveAndDrill generator = new MoveAndDrill();

    @Test
    public void testNullArgument() {
        try {
            generator.generateCommand(null);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmptyArgument() {
        try {
            generator.generateCommand(new ComplexArgument());
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testADrillPlanNoReset() {
        try {
            ComplexArgument complexArgument = new ComplexArgument();
            complexArgument.setPoints(getTestVertexArgument());
            complexArgument.setSpeedXY(15f);
            complexArgument.setSpeedZ(2f);
            String cmd = generator.generateCommand(complexArgument);
            assertNotNull(cmd);
            logger.debug("The command is\r\n{}", cmd);
        } catch (Exception e) {
            logger.error("Error generating the commmand", e);
            fail();
        }
    }


    private List<VertexArgument> getTestVertexArgument() {
        List<VertexArgument> ret = new ArrayList<>();
        float crtX = 10, crtY = 10;
        for (int i = 0; i < 10; i++) {
            crtX += i * 10f;
            crtY += i * 10f;
            VertexArgument vertexArgument = new VertexArgument(crtX, crtY, 20f);
            ret.add(vertexArgument);
        }
        return ret;
    }
}
