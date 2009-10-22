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
package com.mbledug.blojsom.plugin.iptocountry;

/**
 * {@link IpToCountryHelper} provides helper methods to IpToCountry Plugin.
 * @author Cliffano Subagio
 */
final class IpToCountryHelper {

    /**
     * Base number used to calculate IP number.
     */
    private static final int IP_NUMBER_BASE = 256;

    /**
     * Hides default constructor of helper class.
     */
    private IpToCountryHelper() {
    }

    /**
     * Calculates an IP number from the specified IP address,
     * as specified on http://ip-to-country.webhosting.info/node/view/55 .
     * This method expects valid IP address to calculate.
     * @param ipAddress the IP address to calculate the IP number from
     * @return the IP number
     */
    static long calculateIpNumber(final String ipAddress) {

        long ipNumber = 0;
        String[] octets = ipAddress.split("\\.");
        for (int i = 0; i < octets.length; i++) {
            int number = Integer.parseInt(octets[i]);
            ipNumber += number * Math.pow(
                    IP_NUMBER_BASE, octets.length - 1 - i);
        }
        return ipNumber;
    }

    /**
     * Checks if IP address is on the ignored IP addresses array.
     * This method expects valid IP address to check.
     * @param ipAddress the IP address to check
     * @param ignoredIpAddresses an array of ignored IP addresses
     * @return true if IP address is to be ignored, false otherwise
     */
    static boolean isIgnoredIpAddress(
            final String ipAddress, final String[] ignoredIpAddresses) {

        boolean isIgnored = false;
        for (int i = 0; i < ignoredIpAddresses.length; i++) {
            if (ipAddress.equals(ignoredIpAddresses[i])) {
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }
}
