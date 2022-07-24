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
package com.maiereni.cad.rml1.bo;

import com.maiereni.cad.rml1.CommandArgument;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Petre Maierean
 */
public class FloatArgument implements CommandArgument {
    public static final NumberFormat NUMBER_FORMAT;
    public static final String FLOAT_PATTERN = "(\\x20)?(\\d)+(\\x2e\\d)?(\\x20)?";

    static {
        NumberFormat nf = DecimalFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        NUMBER_FORMAT = nf;
    }

    private float f;

    public FloatArgument() {
    }

    public FloatArgument(String s) throws Exception {
        if (StringUtils.isNotBlank(s) && s.matches(FLOAT_PATTERN)) {
            this.f = Float.parseFloat(s.trim());
        } else {
            throw new Exception("A blank string as argument");
        }
    }

    public FloatArgument(float f) {
        this.f = f;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return NUMBER_FORMAT.format(f);
    }
}
