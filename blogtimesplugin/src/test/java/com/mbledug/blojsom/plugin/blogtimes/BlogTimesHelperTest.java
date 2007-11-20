package com.mbledug.blojsom.plugin.blogtimes;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;

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

    public void testGetNumOfLatestEntriesGivesBlogPropertyValue() {
        int numOfLatestEntries = 100;

        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_NUM_OF_LATEST_ENTRIES, "" + numOfLatestEntries);

        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(properties);

        assertEquals(numOfLatestEntries, BlogTimesHelper.getNumOfLatestEntries(blog));
    }

    public void testGetNumOfLatestEntriesWithNullPropertyGivesDefaultValue() {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_NUM_OF_LATEST_ENTRIES, null);

        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(properties);

        assertEquals(BlogTimesHelper.DEFAULT_NUM_OF_LATEST_ENTRIES, BlogTimesHelper.getNumOfLatestEntries(blog));
    }

    public void testGetNumOfLatestEntriesWithInvalidPropertyGivesDefaultValue() {
        Map properties = new HashMap();
        properties.put(BlogTimesPlugin.PROPERTY_NUM_OF_LATEST_ENTRIES, "abc");

        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(properties);

        assertEquals(BlogTimesHelper.DEFAULT_NUM_OF_LATEST_ENTRIES, BlogTimesHelper.getNumOfLatestEntries(blog));
    }

    public void testGetDatesFromEntriesWithEmptyArrayGivesEmptyArrayOfDates() {
        Date[] dates = BlogTimesHelper.getDatesFromEntries(new Entry[]{});
        assertEquals(0, dates.length);
    }
}
