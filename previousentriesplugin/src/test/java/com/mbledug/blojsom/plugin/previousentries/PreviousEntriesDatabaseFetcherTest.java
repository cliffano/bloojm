package com.mbledug.blojsom.plugin.previousentries;

import junit.framework.TestCase;

import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.FetcherException;
import org.hibernate.HibernateException;

public class PreviousEntriesDatabaseFetcherTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testLoadPreviousEntriesRetrievesPreviousEntries() {
        int numPreviousEntries = 10;
        PreviousEntriesDatabaseFetcher fetcher = new PreviousEntriesDatabaseFetcher(mDataFixture.createMockSessionFactory(numPreviousEntries));
        try {
            assertEquals(numPreviousEntries, fetcher.loadPreviousEntries(mDataFixture.createBlog(11), new DatabaseEntry(), numPreviousEntries).length);
        } catch (FetcherException fe) {
            fail("FetcherException shouldn't have been thrown.");
        }
    }

    public void testLoadPreviousEntriesHandlesHibernateException() {
        int numPreviousEntries = 10;
        PreviousEntriesDatabaseFetcher fetcher = new PreviousEntriesDatabaseFetcher(mDataFixture.createMockSessionFactory(new HibernateException("Problem while retrieving entries.")));
        try {
            assertEquals(numPreviousEntries, fetcher.loadPreviousEntries(mDataFixture.createBlog(11), new DatabaseEntry(), numPreviousEntries).length);
            fail("Exception should've been thrown.");
        } catch (FetcherException fe) {
            // expected
        }
    }
}
