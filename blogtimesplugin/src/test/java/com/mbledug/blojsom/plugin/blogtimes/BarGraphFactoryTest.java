package com.mbledug.blojsom.plugin.blogtimes;

import java.util.Calendar;

import junit.framework.TestCase;

public class BarGraphFactoryTest extends TestCase {

    public void testGetBarGraphWithSupportedFlavors() {
        BarGraph barGraphSecondOfMinute = BarGraphFactory.getBarGraph(BlogTimesPlugin.FLAVOR_SECOND_OF_MINUTE);
        assertEquals(Calendar.SECOND, barGraphSecondOfMinute.getCalendarUnit());
        BarGraph barGraphMinuteOfHour = BarGraphFactory.getBarGraph(BlogTimesPlugin.FLAVOR_MINUTE_OF_HOUR);
        assertEquals(Calendar.MINUTE, barGraphMinuteOfHour.getCalendarUnit());
        BarGraph barGraphHourOfDay = BarGraphFactory.getBarGraph(BlogTimesPlugin.FLAVOR_HOUR_OF_DAY);
        assertEquals(Calendar.HOUR_OF_DAY, barGraphHourOfDay.getCalendarUnit());
    }

    public void testGetBarGraphWithNullFlavorGivesDefaultBarGraph() {
        BarGraph barGraph = BarGraphFactory.getBarGraph(null);
        assertEquals(Calendar.HOUR_OF_DAY, barGraph.getCalendarUnit());
        assertEquals(BlogTimesHelper.SECONDS_IN_DAY, barGraph.getScaler());
        assertEquals(2, barGraph.getInterval());
        assertEquals(BlogTimesHelper.MAX_HOUR_IN_DAY, barGraph.getMaxValue());
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
