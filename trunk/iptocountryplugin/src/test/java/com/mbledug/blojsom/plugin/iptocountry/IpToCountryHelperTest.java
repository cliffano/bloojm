package com.mbledug.blojsom.plugin.iptocountry;

import junit.framework.TestCase;

public class IpToCountryHelperTest extends TestCase {

    public void testCalculateIpNumberSuccess() {
        assertEquals(1482184792l, IpToCountryHelper.calculateIpNumber("88.88.88.88"));
        assertEquals(3402287818l, IpToCountryHelper.calculateIpNumber("202.202.202.202"));
    }

    public void testCalculateNullIpNumberGivesNullPointerException() {
        try {
            IpToCountryHelper.calculateIpNumber(null);
            fail("NullPointerException should've been thrown.");
        } catch (NullPointerException npe) {
            // expected
        }
    }

    public void testCalculateInvalidIpNumberGivesNumberFormatException() {
        try {
            IpToCountryHelper.calculateIpNumber("abc");
            fail("NumberFormatException should've been thrown.");
        } catch (NumberFormatException nfe) {
            // expected
        }
    }

    public void testIgnoredIsIpAddressReturnsTrueWhenIpAddressIsInTheIgnoredIpAddressesArray() {
        assertTrue(IpToCountryHelper.isIgnoredIpAddress("111.111.111.111", new String[]{"111.111.111.111", "112.112.112.112", "113.113.113.113"}));
    }

    public void testIgnoredIsIpAddressReturnsFalseWhenIpAddressIsNotInTheIgnoredIpAddressesArray() {
        assertFalse(IpToCountryHelper.isIgnoredIpAddress("101.101.101.101", new String[]{"111.111.111.111", "112.112.112.112", "113.113.113.113"}));
    }

    public void testIgnoredIsIpAddressForNullIpAddressGivesNullPointerException() {
        try {
            IpToCountryHelper.isIgnoredIpAddress(null, new String[]{"111.111.111.111", "112.112.112.112", "113.113.113.113"});
            fail("NullPointerException should've been thrown.");
        } catch (NullPointerException npe) {
            // expected
        }
    }

    public void testIgnoredIsIpAddressWithNullIgnoredIpAddressesArrayGivesNullPointerException() {
        try {
            IpToCountryHelper.isIgnoredIpAddress("111.111.111.111", null);
            fail("NullPointerException should've been thrown.");
        } catch (NullPointerException npe) {
            // expected
        }
    }
}
