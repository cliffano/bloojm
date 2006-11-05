package com.mbledug.blojsom.plugin.blogtimes;

import java.util.HashMap;
import java.util.Properties;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;

public class BlogTimesPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessStoresDatesAsSessionAttribute() {
        Plugin blogTimesPlugin = new BlogTimesPlugin();
        Entry[] entries = DataFixture.createEntryWithDates(
            DataFixture.createRandomDates(10));

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    mDataFixture.createMockHttpServletRequestSetSessionAttribute(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);
            blogTimesPlugin.cleanup();
            blogTimesPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testInitWithValidProperties() {
        Properties properties = DataFixture.createPropertiesWithValidValues();

        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin();
        blogTimesPlugin.setProperties(properties);

        try {
            blogTimesPlugin.init();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testInitWithInvalidPropertiesGivesPluginException() {
        Properties properties = DataFixture.createPropertiesWithInvalidValues();

        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin();
        blogTimesPlugin.setProperties(properties);

        try {
            blogTimesPlugin.init();
            fail("PluginException should've occured.");
        } catch (PluginException pe) {
            // expected PluginException
        }
    }
}
