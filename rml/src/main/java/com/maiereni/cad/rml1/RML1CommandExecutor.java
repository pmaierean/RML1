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

import com.maiereni.cad.rml1.bo.Mode1Command;
import com.maiereni.cad.rml1.bo.Mode2Command;
import com.maiereni.cad.rml1.bo.ModeCommonCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.event.PrintJobListener;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A class to execute an RML1 command
 *
 * @author Petre Maierean
 */
public class RML1CommandExecutor {
    private static final Logger logger = LogManager.getLogger(RML1CommandExecutor.class);
    private String printerName = "Roland MODELA MDX-15";
    private PrintService printService;

    private PrintJobListener printJobListener;

    /**
     * Send a command file
     *
     * @param commandFile
     * @throws Exception
     */
    public void sendCommandFile(File commandFile) throws Exception {
        if (commandFile == null) {
            throw new Exception("Null argument");
        }
        if (!commandFile.isFile()) {
            throw new Exception("Not a command file at " + commandFile.getPath());
        }
        logger.debug("Get command file content from {}", commandFile.getPath());
        String sCommand = FileUtils.readFileToString(commandFile, Charset.defaultCharset());
        sendCommandString(sCommand);
        logger.debug("The command string has been sent");
    }

    /**
     * Execute a command
     *
     * @param name the name of the command selector
     * @param args the arguments for the command
     * @throws Exception exception
     */
    public void sendCommand(String name, String... args) throws Exception {
        String s = generateCommand(name, args);
        sendCommandString(s);
    }

    /**
     * Sends a command string
     *
     * @param commandString
     * @throws Exception
     */
    public void sendCommandString(String commandString) throws Exception {
        if (StringUtils.isNotBlank(commandString)) {
            logger.debug("The command to be send is {}", commandString);
            byte[] bis = commandString.getBytes(StandardCharsets.US_ASCII);
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            SimpleDoc doc = new SimpleDoc(bis, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            getDocPrintJob().print(doc, new HashPrintRequestAttributeSet());
        }
    }

    /**
     * Process a command with a number of arguments. The arguments are convertered
     *
     * @param name the name of a command
     * @param args
     * @return a formatter command
     * @throws Exception
     */
    public String generateCommand(String name, String... args) throws Exception {
        if (StringUtils.isBlank(name)) {
            throw new Exception("The name of the argument cannot be null");
        }
        String ret = null;
        CommandAndLetters commandAndLetters = null;
        if ((commandAndLetters = getModel1Command(name)) != null ||
                (commandAndLetters = getModelCommonCommand(name)) != null) {
            String actualCommand = getArguments(commandAndLetters.letters, args);
            List<CommandArgument> arguments = commandAndLetters.commandFormat.parseArguments(actualCommand);
            ret = commandAndLetters.commandFormat.generateCommand(arguments);
        } else if ((commandAndLetters = getModel2Command(name)) != null) {
            String actualCommand = getArguments(commandAndLetters.letters, args);
            List<CommandArgument> arguments = commandAndLetters.commandFormat.parseArguments(actualCommand);
            ret = commandAndLetters.commandFormat.generateCommand(arguments);
            ret = Mode1Command.callMode.getCommandLetter() + " " + ret;
        }
        return ret;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) throws Exception {
        init(printerName);
    }

    private void init(String printerName) throws Exception {
        if (StringUtils.isNotBlank(printerName)) {
            if (this.printerName == null ||
                    !this.printerName.equals(printerName) ||
                    printService == null) {
                PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
                for (PrintService ps : services) {
                    if (ps.getName().equals(printerName)) {
                        this.printerName = printerName;
                        printService = ps;
                        break;
                    }
                }
                if (printService == null) {
                    throw new Exception("Could not resolve the printer by name: " + printerName);
                }
            }
        }
    }

    public PrintService getPrintService() {
        return printService;
    }

    public DocPrintJob getDocPrintJob() throws Exception {
        if (printService == null) {
            init(printerName);
        }
        DocPrintJob ret = printService.createPrintJob();
        if (printJobListener != null) {
            ret.addPrintJobListener(printJobListener);
        }
        logger.debug("The printer has been found and a print job was created");
        return ret;
    }

    public void setPrintJobListener(PrintJobListener printJobListener) {
        this.printJobListener = printJobListener;
    }

    private String getArguments(String commandLetter, String... args) {
        StringWriter sw = new StringWriter();
        sw.write(commandLetter);
        sw.write(" ");
        if (args != null && args.length > 0) {
            boolean hasComma = false;
            for (String arg : args) {
                if (hasComma) {
                    sw.write(",");
                }
                sw.write(arg);
            }
        }
        sw.write(";");
        return sw.toString();
    }

    private CommandAndLetters getModel1Command(String name) throws Exception {
        CommandAndLetters ret = null;
        for (Mode1Command mode1Command : Mode1Command.values()) {
            if (mode1Command.getName().equals(name)) {
                CommandFormat format = CommandFormatFactory.getFormat(mode1Command);
                ret = new CommandAndLetters(format, mode1Command.getCommandLetter());
                break;
            }
        }
        return ret;
    }

    private CommandAndLetters getModel2Command(String name) throws Exception {
        CommandAndLetters ret = null;
        for (Mode2Command mode1Command : Mode2Command.values()) {
            if (mode1Command.getName().equals(name)) {
                CommandFormat format = CommandFormatFactory.getFormat(mode1Command);
                ret = new CommandAndLetters(format, mode1Command.getCommandLetter());
                break;
            }
        }
        return ret;
    }

    private CommandAndLetters getModelCommonCommand(String name) throws Exception {
        CommandAndLetters ret = null;
        for (ModeCommonCommand mode1Command : ModeCommonCommand.values()) {
            if (mode1Command.getName().equals(name)) {
                CommandFormat format = CommandFormatFactory.getFormat(mode1Command);
                ret = new CommandAndLetters(format, mode1Command.getCommandLetter());
                break;
            }
        }
        return ret;
    }

    private class CommandAndLetters {
        CommandFormat commandFormat;
        String letters;

        public CommandAndLetters(CommandFormat commandFormat, String letters) {
            this.commandFormat = commandFormat;
            this.letters = letters;
        }
    }
}
