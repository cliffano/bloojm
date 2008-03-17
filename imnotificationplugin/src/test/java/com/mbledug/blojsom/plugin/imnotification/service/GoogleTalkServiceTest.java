package com.mbledug.blojsom.plugin.imnotification.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class GoogleTalkServiceTest extends TestCase {

    public void testConstructorWithNullHostThrowsIllegalArgumentException() {
        try {
            new GoogleTalkService(null, "dummyPassword");
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

//    public void testSendViaLiveService() {
//        GoogleTalkService service = new GoogleTalkService("bloojm", "p4ssw0rd");
//        List recipients = new ArrayList();
//        recipients.add("bloojm@gmail.com");
//        try {
//            service.send(recipients, "unit test message");
//        } catch (Exception e) {
//            fail("Exception shouldn't have been thrown.");
//        }
//    }
}
