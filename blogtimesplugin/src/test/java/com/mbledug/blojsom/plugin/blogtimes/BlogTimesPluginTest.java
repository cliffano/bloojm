package com.mbledug.blojsom.plugin.blogtimes;

import java.util.HashMap;

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
}
