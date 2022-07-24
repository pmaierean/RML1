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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.maiereni.cad.rml1.bo.Description;
import com.maiereni.cad.rml1.bo.FormaterCfg;
import com.maiereni.cad.rml1.mode1.DwellFormat;
import com.maiereni.cad.rml1.mode1.SetZ1Z2Format;
import com.maiereni.cad.rml1.mode1.*;
import com.maiereni.cad.rml1.mode2.*;
import com.maiereni.cad.rml1.modec.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Unit test for ResourceUtils
 *
 * @author Petre Maierean
 */
public class ResourceUtilsTest {
    private static final Logger logger = LogManager.getLogger(ResourceUtilsTest.class);
    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    @Test
    public void testCreateConfigutation() {
        try {
            String s = generateSeedConfiguration();
            logger.debug("The seed configuration is:\r\n{}", s);
        } catch (Exception e) {
            logger.error("There was an error creating a seed configuration", e);
            fail("Failed to process");
        }
    }

    @Test
    public void testReadConfigurationFromString() {
        try {
            String s = generateSeedConfiguration();
            Description description = objectMapper.readValue(s, Description.class);
            assertNotNull(description);
        } catch (Exception e) {
            logger.error("There was an error", e);
            fail("Failed to process");
        }
    }

    @Test
    public void testReadConfiguration() {
        try {
            Description description = ResourceUtils.getResources("/description", Locale.ENGLISH, Description.class);
            assertNotNull(description);
            assertTrue(ResourceUtils.isCached("/description", Locale.ENGLISH, Description.class));
        } catch (Exception e) {
            logger.error("There was an error", e);
            fail("Failed to process");
        }
    }

    private String generateSeedConfiguration() throws Exception {
        Description description = new Description();
        description.setMode1(getMode1FormatConfigurations());
        description.setMode2(getMode2FormatConfigurations());
        description.setModec(getModecFormatConfigurations());
        return objectMapper.writeValueAsString(description);
    }

    private List<FormaterCfg> getMode1FormatConfigurations() {
        List<FormaterCfg> ret = new ArrayList<>();
        ret.add(getFormatCfg(SetZ1Z2Format.NAME, "Sets point Z1,Z2 in the workpiece coordinate system"));
        ret.add(getFormatCfg(DrawLineCuttingFormat.NAME, "Cuts line segments from the present tool location to those is it segment"));
        ret.add(getFormatCfg(RelativeDrawLineCuttingFormat.NAME, "Cuts line segment from the present tool position at Z1 by the delta segments"));
        ret.add(getFormatCfg(LinearMovementFormat.NAME, "Performs a  sequence of linear movements on the Z2 height"));
        ret.add(getFormatCfg(RelativeLinearMovementFormater.NAME, "Performs a sequence of delta linear movements on the Z2 height"));
        ret.add(getFormatCfg(CallModeFormat.NAME, "Call a Mode2 command"));
        ret.add(getFormatCfg(HomeMovementFormat.NAME, "Move home"));
        ret.add(getFormatCfg(VelocityFormat.NAME, "Set the speed on movements on the X and Y direction. "));
        ret.add(getFormatCfg(ZVelocityFormat.NAME, "Set the speed of movements on the Z axis."));
        ret.add(getFormatCfg(DwellFormat.NAME, "Wait for a number of msec before making the next command."));
        ret.add(getFormatCfg(ThreeAxisFormat.NAME, "Performs a sequence of linear moves on XYZ simultanously"));
        return ret;
    }

    private List<FormaterCfg> getMode2FormatConfigurations() {
        List<FormaterCfg> ret = new ArrayList<>();
        ret.add(getFormatCfg(DefaultSettingsFormat.NAME, "Get all settings at default"));
        ret.add(getFormatCfg(InitializeFormat.NAME, "Get all settings at default, clean errors, reset coordinates, and get to home"));
        ret.add(getFormatCfg(PlotAbsoluteFormat.NAME, "Change the absolute coordinate mode"));
        ret.add(getFormatCfg(PlotRelativeFormat.NAME, "Change the relative coordinate mode"));
        ret.add(getFormatCfg(PenUpFormat.NAME, "Move the pen up and if XY coordinates are given, move there"));
        ret.add(getFormatCfg(PenDownFormat.NAME, "Move the pen down and if XY coordinates are given, move there"));
        ret.add(getFormatCfg(VelocitySelectionFormat.NAME, "Select the tool speed"));

        return ret;
    }

    private List<FormaterCfg> getModecFormatConfigurations() {
        List<FormaterCfg> ret = new ArrayList<>();
        ret.add(getFormatCfg(com.maiereni.cad.rml1.modec.DwellFormat.NAME, "Wait for a number of milliseconds"));
        ret.add(getFormatCfg(MotorControlFormat.NAME, "Makes the spinner rotate or stop when passed a 0 as argumetn"));
        ret.add(getFormatCfg(NotReadyFormat.NAME, "Sets the printer in non ready state. It can only be started from the pannel"));
        ret.add(getFormatCfg(SetZ0Format.NAME, "Sets the Z0 heigth"));
        ret.add(getFormatCfg(com.maiereni.cad.rml1.modec.SetZ1Z2Format.NAME, "Sets the Z1 and Z2 heights"));
        ret.add(getFormatCfg(RevolutionControlFormat.NAME, "Sets the rotating speed of the spindle motor"));
        ret.add(getFormatCfg(VelocitySelectZFormat.NAME, "Sets the velocity on the Z axis"));
        ret.add(getFormatCfg(ZAxisMoveFormat.NAME, "Performs a movement on the Z axis"));
        ret.add(getFormatCfg(ExtensionAxisMovementFormat.NAME, "This moves (or rotates) the specified axis from the present coordinate or angle to the specified coordinate or angle"));
        ret.add(getFormatCfg(ThreeAxisSimultanousFeedFormat.NAME, "This moves simultaneously along the three axes from the present coordinates to the specified coordinate values."));
        return ret;
    }

    private FormaterCfg getFormatCfg(String name, String description) {
        FormaterCfg ret = new FormaterCfg();
        ret.setDescription(description);
        ret.setName(name);
        return ret;
    }
}
