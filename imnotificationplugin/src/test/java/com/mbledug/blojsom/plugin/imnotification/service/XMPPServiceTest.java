package com.mbledug.blojsom.plugin.imnotification.service;

import junit.framework.TestCase;

public class XMPPServiceTest extends TestCase {

    public void testConstructorWithNullHostThrowsIllegalArgumentException() {
        try {
            new XMPPService(null, new Integer("5222"), "dummyUsername", "dummyPassword");
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructorWithNullUsernameThrowsIllegalArgumentException() {
        try {
            new XMPPService("jabber.org", new Integer("5222"), null, "dummyPassword");
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testSendViaLiveService() {
        XMPPService service = new XMPPService("jabber.org", new Integer("5222"), "bloojm", "password");
        try {
            service.send(new String[]{"bloojm@jabber.org"}, "unit test message");
        } catch (Exception e) {
            fail("Exception shouldn't have been thrown.");
        }
    }

    public void testSendViaLiveServiceWithDefaultPort() {
        XMPPService service = new XMPPService("jabber.org", null, "bloojm", "password");
        try {
            service.send(new String[]{"bloojm@jabber.org"}, "unit test message");
        } catch (Exception e) {
            fail("Exception shouldn't have been thrown.");
        }
    }
}
