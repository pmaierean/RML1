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

import com.maiereni.cad.rml1.CommandArgument;
import com.maiereni.cad.rml1.bo.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * A class that deals with validating Arguments
 *
 * @author Petre Maierean
 */
public class ArgumentValidator {
    private static final float MIN_FLOAT = -8388608.0f;
    private static final float MAX_FLOAT = 8388607.0f;
    private static final int MAX_INT = 32767;
    private static final int MIN_INT = 0;

    /**
     * Validates the number of arguments and to be long
     *
     * @param arguments the list of arguments
     * @param size      the expected size
     * @throws Exception not match
     */
    public void validateArguments(List<CommandArgument> arguments, int size) throws Exception {
        if (size > 0) {
            if (arguments == null || arguments.size() < size) {
                throw new Exception("A number of " + size + " are expected");
            }
        }
    }

    /**
     * Validates the number of arguments and to be long
     *
     * @param arguments the list of arguments
     * @param clazz     the expected class of the argument
     * @param size      the expected size
     * @throws Exception not match
     */
    public <T> void validateArguments(List<CommandArgument> arguments, Class<T> clazz, int size) throws Exception {
        validateArguments(arguments, clazz, null, size);
    }

    /**
     * Validates the number of arguments and to be long
     *
     * @param arguments the list of arguments
     * @param clazz1    one of the expected class of the argument
     * @param clazz2    the other expected class of the argument
     * @param size      the expected size
     * @throws Exception not match
     */
    public <T1, T2> void validateArguments(List<CommandArgument> arguments, Class<T1> clazz1, Class<T2> clazz2, int size) throws Exception {
        validateArguments(arguments, size);
        if (size > 0) {
            if (arguments == null || arguments.size() < size) {
                throw new Exception("A number of " + size + " are expected");
            }
        }
        if (!(arguments == null || clazz1 == null)) {
            for (CommandArgument arg : arguments) {
                validate(arg, clazz1, clazz2);
            }
        }
    }

    /**
     * Ensure that the argument can be converted to a long
     *
     * @param arg
     * @throws Exception
     */
    public <T> void validate(CommandArgument arg, Class<T> clazz) throws Exception {
        validate(arg, clazz, null);
    }

    /**
     * Ensure that the argument can be converted to a long
     *
     * @param arg
     * @param clazz1 expected class
     * @param clazz2 or class
     * @throws Exception
     */
    public <T1, T2> void validate(CommandArgument arg, Class<T1> clazz1, Class<T2> clazz2) throws Exception {
        if (arg == null) {
            throw new Exception("The argument is blank");
        }
        if (clazz1 == null) {
            throw new Exception("No class to match");
        }
        if (!(clazz1.isInstance(arg))) {
            validate(arg, clazz2, null);
        }
        if (clazz1.equals(IntArgument.class)) {
            IntArgument argument = (IntArgument) arg;
            validate(argument.getValue(), null);
        } else if (clazz1.equals(FloatArgument.class)) {
            FloatArgument argument = (FloatArgument) arg;
            validate(argument.getF(), null);
        } else if (clazz1.equals(PairOfFloatsArgument.class)) {
            PairOfFloatsArgument argument = (PairOfFloatsArgument) arg;
            validate(argument.getX(), "The argument x is out of bound");
            validate(argument.getY(), "The argument y is out of bound");
        } else if (clazz1.equals(VertexArgument.class)) {
            VertexArgument argument = (VertexArgument) arg;
            validate(argument.getX(), "The argument x is out of bound");
            validate(argument.getY(), "The argument y is out of bound");
            validate(argument.getZ(), "The argument z is out of bound");
        } else if (clazz1.equals(AxisArgument.class)) {
            AxisArgument argument = (AxisArgument) arg;
            validateOptional(argument.getX(), "The argument x is out of bound");
            validateOptional(argument.getY(), "The argument y is out of bound");
            validateOptional(argument.getZ(), "The argument z is out of bound");
            validateOptional(argument.getA(), "The argument a is out of bound");
            if (StringUtils.isBlank(argument.toString())) {
                throw new Exception("The axis float argument has no coordinateSet");
            }
        } else if (clazz1.equals(CallArgument.class)) {
            CallArgument callArgument = (CallArgument) arg;
            if (callArgument.getMode2Command() == null) {
                throw new Exception("No call mode2 specified");
            }
        }
    }

    /**
     * Validates the boundaries of an integer. The value must be between MAX_INT and MIN_INT
     *
     * @param i    the integer to validate
     * @param text the text to used in the exception
     * @throws Exception thrown when exceeding the boundaries
     */
    public void validate(int i, String text) throws Exception {
        if (i > MAX_INT || i < MIN_INT) {
            throw new Exception(text != null ? text : "The argument of type int is out of bound");
        }
    }

    /**
     * Validates the boundaries of an optional float. The value must be between MAX_FLOAT and MIN_FLOAT
     *
     * @param f    the float to validate. It can be null
     * @param text the text to used in the exception
     * @throws Exception thrown when exceeding the boundaries
     */
    public void validateOptional(Float f, String text) throws Exception {
        if (f != null) {
            if (f > MAX_FLOAT || f < MIN_FLOAT) {
                throw new Exception(text != null ? text : "The argument of type float is out of bound");
            }
        }
    }

    /**
     * Validates the boundaries of a float. The value must be between MAX_FLOAT and MIN_FLOAT
     *
     * @param f    the float to validate
     * @param text the text to used in the exception
     * @throws Exception thrown when exceeding the boundaries
     */
    public void validate(float f, String text) throws Exception {
        if (f > MAX_FLOAT || f < MIN_FLOAT) {
            throw new Exception(text != null ? text : "The argument of type float is out of bound");
        }
    }

}
