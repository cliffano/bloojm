package com.mbledug.blojsom.plugin.markdownj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.easymock.classextension.EasyMock;

public class MarkdownJPluginTest extends TestCase {

    public void testProcessConvertsEntriesDescWithMarkdownExtension() {

        Plugin markdownJPlugin = new MarkdownJPlugin();

        String originalDesc = "![alt text](/path/img.jpg \"Title\")";
        String convertedDesc = "<p><img src=\"/path/img.jpg\" alt=\"alt text\" title=\"Title\" /></p>\n";

        Entry entry = new DatabaseEntry();
        entry.setPostSlug(MarkdownJPlugin.MARKDOWN_EXTENSION);
        entry.setDescription(originalDesc);
        Entry[] entries = new Entry[]{entry};

        try {
            markdownJPlugin.init();

            entries = markdownJPlugin.process(
                    (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                    (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
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

        Map metaData = new HashMap();
        metaData.put(MarkdownJPlugin.METADATA_RUN_MARKDOWN, "true");
        Entry entry = new DatabaseEntry();
        entry.setMetaData(metaData);
        entry.setDescription(originalDesc);
        entry.setPostSlug("");
        Entry[] entries = new Entry[]{entry};

        try {
            markdownJPlugin.init();
            entries = markdownJPlugin.process(
                    (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class),
                    (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class),
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

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        Plugin markdownJPlugin = new MarkdownJPlugin();

        String originalDesc = "An [example](http://url.com/ \"Title\")";

        Entry entry = new DatabaseEntry();
        entry.setDescription(originalDesc);
        entry.setPostSlug("");
        entry.setMetaData(new HashMap());
        Entry[] entries = new Entry[]{entry};

        try {
            markdownJPlugin.init();

            EasyMock.replay(new Object[]{request, response});
            entries = markdownJPlugin.process(
                    request,
                    response,
                    new DatabaseBlog(),
                    new HashMap(),
                    entries);
            EasyMock.verify(new Object[]{request, response});

            markdownJPlugin.cleanup();
            markdownJPlugin.destroy();
        } catch (PluginException pe) {
            fail("PluginException should not occur: " + pe);
        }

        assertEquals(originalDesc, entries[0].getDescription());
    }
}
