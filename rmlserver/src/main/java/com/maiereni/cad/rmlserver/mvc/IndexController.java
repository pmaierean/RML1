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
package com.maiereni.cad.rmlserver.mvc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The base controller
 *
 * @author Petre Maierean
 */
@Controller
public class IndexController {
    private static final Logger logger = LogManager.getLogger(IndexController.class);

    @GetMapping(path = "/")
    public String getDefault(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.debug("Get the index");
        return "index";
    }
    
    @GetMapping(path = "/index.html")
    public String getIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.debug("Get the index");
        return "index";
    }

    @GetMapping(path = "/error.html")
    public String getError(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.debug("Get the error");
        return "error";
    }
}
