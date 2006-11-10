package com.mbledug.blojsom.plugin.blogtimes;

import junit.framework.TestCase;

public class BarGraphFactoryTest extends TestCase {

    public void testGetBarGraphWithSupportedFlavors() {
        BarGraph barGraphSecondOfMinute = BarGraphFactory.getBarGraph(BlogTimesPlugin.FLAVOR_SECOND_OF_MINUTE);
        assertNotNull(barGraphSecondOfMinute);
        BarGraph barGraphMinuteOfHour = BarGraphFactory.getBarGraph(BlogTimesPlugin.FLAVOR_MINUTE_OF_HOUR);
        assertNotNull(barGraphMinuteOfHour);
        BarGraph barGraphHourOfDay = BarGraphFactory.getBarGraph(BlogTimesPlugin.FLAVOR_HOUR_OF_DAY);
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
