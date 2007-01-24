package com.mbledug.blojsom.plugin.gravatar;

import com.mbledug.blojsom.plugin.gravatar.GravatarHelper;

import junit.framework.TestCase;

public class GravatarHelperTest extends TestCase {

    public void testGetGravatarIdSuccess() {

        try {
            String gravatarId = GravatarHelper.getGravatarId(DataFixture.EMAIL);
            assertNotNull(gravatarId);
            assertEquals(DataFixture.EXPECTED_GRAVATAR_ID, gravatarId);
        } catch(Exception e) {
            fail("Email: " + DataFixture.EMAIL + ", should generate Gravatar ID: " + DataFixture.EXPECTED_GRAVATAR_ID);
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