package com.mbledug.blojsom.plugin.blogtimes;

import junit.framework.TestCase;

public class BarGraphFactoryTest extends TestCase {

    public void testGetBarGraphWithSupportedFlavors() {
        BarGraph barGraphSecondOfMinute = BarGraphFactory.getBarGraph("second-of-minute");
        assertNotNull(barGraphSecondOfMinute);
        BarGraph barGraphMinuteOfHour = BarGraphFactory.getBarGraph("minute-of-hour");
        assertNotNull(barGraphMinuteOfHour);
        BarGraph barGraphHourOfDay = BarGraphFactory.getBarGraph("hour-of-day");
        assertNotNull(barGraphHourOfDay);
    }

    public void testGetBarGraphWithNullFlavorGivesDefaultBarGraph() {
        BarGraph defaultBarGraph = BarGraphFactory.getBarGraph(null);
        assertNotNull(defaultBarGraph);
    }

    public void testGetBarGraphWithUnsupportedFlavorGivesIllegalArgumentException() {
        try {
            BarGraph dummyBarGraph = BarGraphFactory.getBarGraph("dummy");
            fail("Unsupported flavor shouldn't return a BarGraph: " + dummyBarGraph);
        } catch (IllegalArgumentException iae) {
            // IllegalArgumentException is expected
        }
    }
}
