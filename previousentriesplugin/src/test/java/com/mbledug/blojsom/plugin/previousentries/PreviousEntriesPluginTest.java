package com.mbledug.blojsom.plugin.previousentries;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.FetcherException;
import org.blojsom.plugin.PluginException;
import org.easymock.classextension.EasyMock;

public class PreviousEntriesPluginTest extends TestCase {

    public void testProcessAddsPreviousEntriesToContextWhenTheresOnlyOneEntry() throws Exception {

        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[] {entry};

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        PreviousEntriesDatabaseFetcher fetcher = (PreviousEntriesDatabaseFetcher) EasyMock.createStrictMock(PreviousEntriesDatabaseFetcher.class);
        EasyMock.expect(fetcher.loadPreviousEntries(blog, entry, numPreviousEntries)).andReturn(createDatabaseEntry(numPreviousEntries));
        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(fetcher);

        EasyMock.replay(new Object[]{fetcher, request, response});

        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    request,
                    response,
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

        EasyMock.verify(new Object[]{fetcher, request, response});
    }

    public void testProcessDoesntAddPreviousEntriesToContextWhenTheresMoreThanOneEntry() throws Exception {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[] {entry, entry};

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        PreviousEntriesDatabaseFetcher fetcher = (PreviousEntriesDatabaseFetcher) EasyMock.createStrictMock(PreviousEntriesDatabaseFetcher.class);
        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(fetcher);

        EasyMock.replay(new Object[]{fetcher, request, response});

        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    request,
                    response,
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        assertNull(context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES));

        EasyMock.verify(new Object[]{fetcher, request, response});
    }

    public void testProcessDoesntAddPreviousEntriesToContextWhenTheresNoEntry() throws Exception {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[0];

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        PreviousEntriesDatabaseFetcher fetcher = (PreviousEntriesDatabaseFetcher) EasyMock.createStrictMock(PreviousEntriesDatabaseFetcher.class);
        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(fetcher);

        EasyMock.replay(new Object[]{fetcher, request, response});

        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    request,
                    response,
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        assertNull(context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES));

        EasyMock.verify(new Object[]{fetcher, request, response});
    }

    public void testProcessDoesntAddPreviousEntriesWhenFetcherExceptionOccurs() throws Exception {
        int numPreviousEntries = 24;
        HashMap context = new HashMap();
        Properties properties = new Properties();
        properties.put(PreviousEntriesPlugin.PROPERTY_PREVIOUS_ENTRIES_NUM, "" + numPreviousEntries);
        DatabaseBlog blog = createBlog(20);
        blog.setProperties(properties);
        DatabaseEntry entry = new DatabaseEntry();
        entry.setDate(new Date());
        Entry[] entries = new Entry[] {entry};

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        PreviousEntriesDatabaseFetcher fetcher = (PreviousEntriesDatabaseFetcher) EasyMock.createStrictMock(PreviousEntriesDatabaseFetcher.class);
        EasyMock.expect(fetcher.loadPreviousEntries(blog, entry, numPreviousEntries)).andThrow(new FetcherException());
        PreviousEntriesPlugin previousEntriesPlugin = new PreviousEntriesPlugin(fetcher);

        EasyMock.replay(new Object[]{fetcher, request, response});
        try {
            previousEntriesPlugin.init();
            previousEntriesPlugin.process(
                    request,
                    response,
                    blog,
                    context,
                    entries);
            previousEntriesPlugin.cleanup();
            previousEntriesPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected exception: " + pe);
        }

        assertNull(context.get(PreviousEntriesPlugin.CONTEXT_PREVIOUS_ENTRIES));

        EasyMock.verify(new Object[]{fetcher, request, response});
    }

    DatabaseBlog createBlog(int id) {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setId(new Integer(id));
        return blog;
    }

    DatabaseEntry[] createDatabaseEntry(int numOfEntries) {
    	DatabaseEntry[] entries = new DatabaseEntry[numOfEntries];
        for (int i = 0; i < numOfEntries; i++) {
            entries[i] = new DatabaseEntry();
        }
        return entries;
    }
}
