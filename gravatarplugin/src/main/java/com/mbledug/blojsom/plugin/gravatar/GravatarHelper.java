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
package com.mbledug.blojsom.plugin.gravatar;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * GravatarHelper provides helper methods to Gravatar Plugin.
 * @author Cliffano Subagio
 */
final class GravatarHelper {

    /**
     * Algorithm used to digest the email value.
     */
    private static final String DIGEST_ALGORITHM = "MD5";

    /**
     * Charset name used to retrieve the byte array from email String.
     */
    private static final String CHARSET_NAME = "CP1252";

    /**
     * Bitwise AND value to applied against the digested email byte.
     */
    private static final int BIT_AND = 0xFF;

    /**
     * Bitwise INCLUSIVE OR value to applied against the digested email byte.
     */
    private static final int BIT_INC_OR = 0x100;

    /**
     * Number of characters from the hexadecimal result from the bit operation.
     */
    private static final int SIZE = 3;

    /**
     * Hides default constructor of helper class.
     */
    private GravatarHelper() {
    }

    /**
     * Generates Gravatar ID based on the email.
     * Gravatar ID is a calculation of MD5Hex of the email, using Gravatar.com's
     * Java implementation sample http://gravatar.com/implement.php#section_3_4
     * This method expects non-null email and will throw
     * an IllegalArgumentException when null email is passed.
     * @param email the email to generate the Gravatar ID from
     * @return Gravatar ID, MD5Hex value of the email
     * @throws NoSuchAlgorithmException when algorithm does not exist
     * @throws UnsupportedEncodingException when encoding is not supported
     */
    static String getGravatarId(final String email)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        if (email == null) {
            throw new IllegalArgumentException("Unable to get Gravatar ID "
                    + "with null email.");
        }

        MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
        byte[] digestedEmail = md.digest(email.getBytes(CHARSET_NAME));
        StringBuffer gravatarId = new StringBuffer();
        for (int i = 0; i < digestedEmail.length; ++i) {
            gravatarId
                    .append(Integer.toHexString(
                        (digestedEmail[i] & BIT_AND) | BIT_INC_OR)
                    .substring(1, SIZE));
        }
        return gravatarId.toString();
    }
}
