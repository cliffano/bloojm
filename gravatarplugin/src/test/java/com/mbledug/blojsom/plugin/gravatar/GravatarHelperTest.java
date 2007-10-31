package com.mbledug.blojsom.plugin.gravatar;

import junit.framework.TestCase;

public class GravatarHelperTest extends TestCase {

    public void testGetGravatarIdSuccess() {

        try {
            String gravatarId = GravatarHelper.getGravatarId("foo@bar.com");
            assertNotNull(gravatarId);
            assertEquals("f3ada405ce890b6f8204094deb12d8a8", gravatarId);
        } catch(Exception e) {
            fail("Email: " + "foo@bar.com" + ", should generate Gravatar ID: " + "f3ada405ce890b6f8204094deb12d8a8");
        }
    }

    public void testGetGravatarIdWithNullEmailGivesIllegalArgumentException() {

        try {
            String gravatarId = GravatarHelper.getGravatarId(null);
            fail("Null email should not generate a Gravatar ID: " + gravatarId);
        } catch(IllegalArgumentException iae) {
            // IllegalArgumentException should be thrown
        } catch(Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
    }
}