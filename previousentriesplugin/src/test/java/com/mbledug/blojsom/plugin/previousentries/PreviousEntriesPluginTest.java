package com.mbledug.blojsom.plugin.previousentries;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.FetcherException;
import org.blojsom.plugin.PluginException;

public class PreviousEntriesPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testProcessAddsPreviousEntriesToContextWhenTheresOnlyOneEntry() {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = mDataFixture.createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[] {entry};

        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(mDataFixture.createMockFetcher(numPreviousEntries));
        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        DatabaseEntry[] previousEntries = (DatabaseEntry[]) context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES);
        assertNotNull(previousEntries);
        assertEquals(numPreviousEntries, previousEntries.length);
    }

    public void testProcessDoesntAddPreviousEntriesToContextWhenTheresMoreThanOneEntry() {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = mDataFixture.createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[] {entry, entry};

        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(mDataFixture.createMockFetcher(numPreviousEntries));
        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        assertNull(context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES));
    }

    public void testProcessDoesntAddPreviousEntriesToContextWhenTheresNoEntry() {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = mDataFixture.createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[0];

        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(mDataFixture.createMockFetcher(numPreviousEntries));
        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        assertNull(context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES));
    }

    public void testProcessDoesntAddPreviousEntriesWhenFetcherExceptionOccurs() {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = mDataFixture.createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[0];

        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(mDataFixture.createMockFetcher(new FetcherException()));
        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    mDataFixture.createMockHttpServletRequest(),
                    mDataFixture.createMockHttpServletResponse(),
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        assertNull(context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES));
    }
}
