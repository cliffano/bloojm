package com.mbledug.blojsom.plugin.blogtimes;

import java.awt.Color;

import junit.framework.TestCase;

public class BlogTimesHelperTest extends TestCase {

    public void testHexToColorWithLowercaseHex() {
        Color color = BlogTimesHelper.hexToColor("ff0000");
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());
    }

    public void testHexToColorWithUppercaseHex() {
        Color color = BlogTimesHelper.hexToColor("FFFFFF");
        assertEquals(255, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(255, color.getBlue());
    }

    public void testHexToColorWithNullHexGivesIllegalArgumentException() {
        try {
            Color color = BlogTimesHelper.hexToColor(null);
            fail("Null hex color shouldn't convert to Color: " + color);
        } catch (IllegalArgumentException iae) {
            // IllegalArgumentException is expected to be thrown
        }
    }

    public void testHexToColorWithInvalidHexGivesNumberFormatException() {
        try {
            Color color = BlogTimesHelper.hexToColor("XYZZMN");
            fail("Invalid hex color shouldn't convert to Color: " + color);
        } catch (NumberFormatException nfe) {
            // NumberFormatException is expected to be thrown
        }
    }
}
