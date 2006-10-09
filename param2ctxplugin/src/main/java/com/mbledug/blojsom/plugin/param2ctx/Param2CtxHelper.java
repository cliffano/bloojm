/**
 * Copyright (c) 2005-2006, Cliffano Subagio
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of Mbledug nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mbledug.blojsom.plugin.param2ctx;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Param2CtxHelper provides helper methods to Param2Ctx Plugin.
 * @author Cliffano Subagio
 */
final class Param2CtxHelper {

    /**
     * Logger for Param2CtxHelper.
     */
    private static final Log LOG = LogFactory.getLog(Param2CtxHelper.class);

    /**
     * Prefix string to be prepended to the request parameter name.
     */
    static final String PREFIX = "param_";

    /**
     * Hides default constructor of helper class.
     */
    private Param2CtxHelper() {
    }

    /**
     * Puts all request parameters name-value pairs into the context map.
     * The request parameter name will be prepended with PREFIX and used as the
     * key in the context map. The request parameter value will be copied as is
     * as the context map value.
     * This method expects non-null request and context and will throw
     * an IllegalArgumentException when either one is null.
     * @param httpServletRequest the request to get the parameters from
     * @param context the context map destination to put to
     */
    static void putParamsToContext(
            final HttpServletRequest httpServletRequest,
            final Map context) {

        if (httpServletRequest == null || context == null) {
            throw new IllegalArgumentException("Unable to put request "
                    + "parameters into context map with either null request "
                    + " or context map. Request: " + httpServletRequest
                    + ", context: " + context);
        }

        Enumeration paramNames = httpServletRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            String value = httpServletRequest.getParameter(name);
            context.put(PREFIX + name, value);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding request parameter name: " + name
                        + ", value: " + value + ", into context with name: "
                        + PREFIX + name);
            }
        }
    }
}
