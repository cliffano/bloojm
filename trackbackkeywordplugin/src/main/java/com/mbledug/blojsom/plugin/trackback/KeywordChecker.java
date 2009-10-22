/**
 * Copyright (c) 2005-2007, Cliffano Subagio
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
 *   * Neither the name of Studio Cliffano nor the names of its contributors
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
package com.mbledug.blojsom.plugin.trackback;

/**
 * {@link KeywordChecker} checks the existence of a keyword within a text.
 * @author Cliffano Subagio
 */
class KeywordChecker {

    /**
     * The keywords to be checked for existence.
     */
    private String[] mKeywords;

    /**
     * Creates instance of {@link KeywordChecker} with specified keywords.
     * @param keywords an array of keywords to be checked for existence
     */
    KeywordChecker(final String[] keywords) {
        mKeywords = keywords;
    }

    /**
     * Checks whether the text has at least one keyword.
     * @param text the text to check
     * @return whether the text has at least one keyword
     */
    final boolean hasAtLeastOneKeyword(final String text) {
        boolean hasAtLeastOneKeyword = false;
        for (int i = 0; i < mKeywords.length; i++) {
            if (text.indexOf(mKeywords[i]) >= 0) {
                hasAtLeastOneKeyword = true;
                break;
            }
        }
        return hasAtLeastOneKeyword;
    }

    /**
     * Checks whether the text has all keywords.
     * @param text the text to check
     * @return whether the text has all keywords
     */
    final boolean hasAllKeywords(final String text) {
        boolean hasAllKeywords = true;
        for (int i = 0; i < mKeywords.length; i++) {
            if (text.indexOf(mKeywords[i]) == -1) {
                hasAllKeywords = false;
                break;
            }
        }
        return hasAllKeywords;
    }
}
