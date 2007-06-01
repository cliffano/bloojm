package com.mbledug.blojsom.plugin.markdownj;

import java.util.HashMap;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;

public class MarkdownJPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessConvertsEntriesDescWithMarkdownExtension() {
        Plugin markdownJPlugin = new MarkdownJPlugin();

        String originalDesc = "![alt text](/path/img.jpg \"Title\")";
        String convertedDesc = "<p><img src=\"/path/img.jpg\" alt=\"alt text\" title=\"Title\" /></p>\n";

        Entry entry = mDataFixture.createEntryWithMarkdownExtension(originalDesc);
        Entry[] entries = new Entry[]{entry};

        try {
            markdownJPlugin.init();
            entries = markdownJPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);
            markdownJPlugin.cleanup();
            markdownJPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        assertEquals(convertedDesc, entries[0].getDescription());
    }

    public void testProcessConvertsEntriesDescWithMarkdownMetaData() {
        Plugin markdownJPlugin = new MarkdownJPlugin();

        String originalDesc = "An [example](http://url.com/ \"Title\")";
        String convertedDesc = "<p>An <a href=\"http://url.com/\" title=\"Title\">example</a></p>\n";

        Entry entry = mDataFixture.createEntryWithMarkdownMetaData(originalDesc);
        Entry[] entries = new Entry[]{entry};

        try {
            markdownJPlugin.init();
            entries = markdownJPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);
            markdownJPlugin.cleanup();
            markdownJPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        assertEquals(convertedDesc, entries[0].getDescription());
    }

    public void testProcessDoesNotConvertEntriesDescWithoutMarkdownConfiguration() {
        Plugin markdownJPlugin = new MarkdownJPlugin();

        String originalDesc = "An [example](http://url.com/ \"Title\")";

        Entry entry = new DatabaseEntry();
        entry.setDescription(originalDesc);
        entry.setPostSlug("");
        entry.setMetaData(new HashMap());
        Entry[] entries = new Entry[]{entry};

        try {
            markdownJPlugin.init();
            entries = markdownJPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);
            markdownJPlugin.cleanup();
            markdownJPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        assertEquals(originalDesc, entries[0].getDescription());
    }
}
