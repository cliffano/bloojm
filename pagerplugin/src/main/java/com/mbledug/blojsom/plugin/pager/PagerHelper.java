/**
 * Copyright (c) 2007, Cliffano Subagio
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
package com.mbledug.blojsom.plugin.pager;

import org.apache.commons.lang.StringUtils;

/**
 * {@link PagerHelper} provides helper methods to Pager Plugin.
 * @author Cliffano Subagio
 */
final class PagerHelper {

    /**
     * Default page number.
     */
    static final int DEFAULT_PAGE_NUM = 1;

    /**
     * Hides default constructor of helper class.
     */
    private PagerHelper() {
    }

    /**
     * Gets the current page number based on page-num param value.
     * If page-num is null then current page will be set to default
     * page 1.
     * @param pageNumParamValue page-num param value
     * @return the current page number
     */
    static int getCurrentPage(final String pageNumParamValue) {

        if (pageNumParamValue != null
                && (!StringUtils.isNumeric(pageNumParamValue)
                        || Integer.parseInt(pageNumParamValue) <= 0)) {
            throw new IllegalArgumentException("Invalid page-num param value: "
                    + pageNumParamValue
                    + ", page-num should be numeric greater than or equal to 1."
                    );
        }

        int currentPage;
        if (pageNumParamValue == null) {
            currentPage = DEFAULT_PAGE_NUM;
        } else {
            currentPage = Integer.parseInt(pageNumParamValue);
        }
        return currentPage;
    }

    /**
     * Gets the number of total pages to navigate.
     * @param totalEntries number of total entries to navigate
     * @param entriesPerPage number of entries per page
     * @return the number of total pages
     */
    static int getTotalPages(final int totalEntries, final int entriesPerPage) {

        if (totalEntries < 0) {
            throw new IllegalArgumentException("Invalid total entries: "
                    + totalEntries
                    + ", total entries should be greater than or equals to 0.");
        }

        if (entriesPerPage <= 0) {
            throw new IllegalArgumentException("Invalid entries per page: "
                    + entriesPerPage
                    + ", entries per page should be greater than or "
                    + " equals to 1.");
        }

        return new Double(Math.ceil(
                new Integer(totalEntries).doubleValue() / entriesPerPage))
                .intValue();
    }
}
