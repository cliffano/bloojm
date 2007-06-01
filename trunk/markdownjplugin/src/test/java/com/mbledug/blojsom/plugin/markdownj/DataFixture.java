package com.mbledug.blojsom.plugin.markdownj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseEntry;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }

    Entry createEntryWithMarkdownExtension(String description) {
        Entry entry = new DatabaseEntry();
        entry.setPostSlug(MarkdownJPlugin.MARKDOWN_EXTENSION);
        entry.setDescription(description);
        return entry;
    }

    Entry createEntryWithMarkdownMetaData(String description) {
        Map metaData = new HashMap();
        metaData.put(MarkdownJPlugin.METADATA_RUN_MARKDOWN, "true");
        Entry entry = new DatabaseEntry();
        entry.setMetaData(metaData);
        entry.setDescription(description);
        entry.setPostSlug("");
        return entry;
    }
}
