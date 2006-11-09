package com.mbledug.blojsom.plugin.blogtimes;

import java.util.HashMap;
import java.util.Map;

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

    public void testProcessStoresBarGraphImageCreatorAndDatesAsSessionAttribute() {
        Entry[] entries = DataFixture.createEntryWithDates(
            DataFixture.createRandomDates(10));
        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(new HashMap());

        Plugin blogTimesPlugin = new BlogTimesPlugin();

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    mDataFixture.createMockHttpServletRequestSetSessionAttribute(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    new HashMap(),
                    entries);
            blogTimesPlugin.cleanup();
            blogTimesPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessWithValidBlogProperties() {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(DataFixture.createPropertiesWithValidValues());
        Entry[] entries = DataFixture.createEntryWithDates(
                DataFixture.createRandomDates(10));

        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin();

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    mDataFixture.createMockHttpServletRequestSetSessionAttribute(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    new HashMap(),
                    entries);
            blogTimesPlugin.cleanup();
            blogTimesPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }
    }

    public void testProcessWithInvalidPropertiesGivesPluginException() {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setProperties(DataFixture.createPropertiesWithInvalidValues());
        Entry[] entries = DataFixture.createEntryWithDates(
                DataFixture.createRandomDates(10));

        BlogTimesPlugin blogTimesPlugin = new BlogTimesPlugin();

        try {
            blogTimesPlugin.init();
            entries = blogTimesPlugin.process(
                    mDataFixture.createMockHttpServletRequestSetSessionAttribute(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    new HashMap(),
                    entries);
            fail("PluginException should've occured.");
        } catch (PluginException pe) {
            // expected PluginException
        }
    }
}
