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

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.CommandFormat;
import com.maiereni.cad.rml1.CommandFormatFactory;
import com.maiereni.cad.rml1.ComplexCommandFormat;
import com.maiereni.cad.rml1.bo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains individual command writers
 *
 * @author Petre Maierean
 */
public abstract class AbstractComplexCommands implements ComplexCommandFormat {
    protected final Logger logger = LogManager.getLogger(getClass());

    protected String getMotorOnOff(boolean b) throws Exception {
        CommandFormat commandFormat = CommandFormatFactory.getFormat(ModeCommonCommand.motorControl);
        List<CommandArgument> commandArguments = new ArrayList<>();
        IntArgument arg = new IntArgument(b ? 1 : 0);
        commandArguments.add(arg);
        return commandFormat.generateCommand(commandArguments);
    }

    protected String getInitialize() throws Exception {
        return "^" + CommandFormatFactory.getFormat(Mode2Command.initialize).generateCommand(null);
    }

    protected String drill(Float z0, Float z1) throws Exception {
        StringWriter sw = new StringWriter();
        List<CommandArgument> commandArguments = new ArrayList<>();
        CommandFormat commandFormat = CommandFormatFactory.getFormat(ModeCommonCommand.zAxisMovement);
        if (z1 != null) {
            commandArguments.clear();
            commandArguments.add(new FloatArgument(z1));
            sw.write(commandFormat.generateCommand(commandArguments));
        }
        if (z0 != null) {
            commandArguments.clear();
            commandArguments.add(new FloatArgument(z0));
            sw.write(commandFormat.generateCommand(commandArguments));
        }
        return sw.toString();
    }

    protected String moveXY(Float x, Float y) throws Exception {
        String ret = "";
        if (!(x == null || y == null)) {
            List<CommandArgument> commandArguments = new ArrayList<>();
            PairOfFloatsArgument xy = new PairOfFloatsArgument(x, y);
            commandArguments.add(xy);
            ret = CommandFormatFactory.getFormat(Mode1Command.linearMovement).generateCommand(commandArguments);
        }
        return ret;
    }

    protected String getSpeedZ(Float argument) throws Exception {
        StringWriter sw = new StringWriter();
        if (argument != null) {
            List<CommandArgument> commandArguments = new ArrayList<>();
            FloatArgument speedXY = new FloatArgument(argument);
            commandArguments.add(speedXY);
            sw.write(CommandFormatFactory.getFormat(Mode1Command.setZVelocity).generateCommand(commandArguments));
            //sw.write(CommandFormatFactory.getFormat(ModeCommonCommand.velocitySelectZ).generateCommand(commandArguments));
        }
        return sw.toString();
    }

    protected String getSpeedXY(Float argument) throws Exception {
        String ret = "";
        if (argument != null) {
            List<CommandArgument> commandArguments = new ArrayList<>();
            FloatArgument speedXY = new FloatArgument(argument);
            commandArguments.add(speedXY);
            ret = CommandFormatFactory.getFormat(Mode1Command.setVelocity).generateCommand(commandArguments);
        }
        return ret;
    }

    protected String getHomeCommand(boolean b) throws Exception {
        String ret = "";
        if (b) {
            ret = CommandFormatFactory.getFormat(Mode1Command.homeMovement).generateCommand(null);
        }
        return ret;
    }

    protected String setZ1Z2(Long z1, Long z2) throws Exception {
        String ret = "";
        if (z1 != null) {
            List<CommandArgument> args = new ArrayList<>();
            args.add(new LongArgument(z1));
            if (z2 != null) {
                args.add(new LongArgument(z2));
            }
            ret = CommandFormatFactory.getFormat(Mode1Command.setZ1Z2).generateCommand(args);
        }
        return ret;
    }


    protected String getPenUpCommand(VertexArgument vertex) throws Exception {
        List<CommandArgument> arguments = new ArrayList<>();
        arguments.add(new CallArgument(Mode2Command.penUp));
        if (vertex != null) {
            PairOfFloatsArgument pairOfFloatsArgument = new PairOfFloatsArgument();
            pairOfFloatsArgument.setX(vertex.getX());
            pairOfFloatsArgument.setY(vertex.getY());
            arguments.add(pairOfFloatsArgument);
        }
        return CommandFormatFactory.getFormat(Mode1Command.callMode).generateCommand(arguments);
    }

    protected String getPenDownCommand(VertexArgument vertex) throws Exception {
        List<CommandArgument> arguments = new ArrayList<>();
        arguments.add(new CallArgument(Mode2Command.penDown));
        if (vertex != null) {
            PairOfFloatsArgument pairOfFloatsArgument = new PairOfFloatsArgument();
            pairOfFloatsArgument.setX(vertex.getX());
            pairOfFloatsArgument.setY(vertex.getY());
            arguments.add(pairOfFloatsArgument);
        }
        return CommandFormatFactory.getFormat(Mode1Command.callMode).generateCommand(arguments);
    }

}
