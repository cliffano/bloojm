package com.mbledug.blojsom.plugin.previousentries;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.FetcherException;
import org.easymock.classextension.EasyMock;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

public class PreviousEntriesDatabaseFetcherTest extends TestCase {

    public void testLoadPreviousEntriesRetrievesPreviousEntries() {
        int numPreviousEntries = 10;

        Query query = (Query) EasyMock.createStrictMock(Query.class);
        EasyMock.expect(query.setInteger("blogId", 11)).andReturn(query);
        EasyMock.expect(query.setTimestamp("entryDate", null)).andReturn(query);
        EasyMock.expect(query.setFirstResult(1)).andReturn(query);
        EasyMock.expect(query.setMaxResults(10)).andReturn(query);
        EasyMock.expect(query.list()).andReturn(createDatabaseEntry(numPreviousEntries));

        Transaction transaction = (Transaction) EasyMock.createStrictMock(Transaction.class);
        transaction.commit();

        Connection connection = (Connection) EasyMock.createStrictMock(Connection.class);

        Session session = (Session) EasyMock.createStrictMock(Session.class);
        EasyMock.expect(session.beginTransaction()).andReturn(transaction);
        EasyMock.expect(session.createQuery("from DatabaseEntry where blog_id = :blogId and entry_date <= :entryDate order by entry_date desc")).andReturn(query);
        EasyMock.expect(session.close()).andReturn(connection);

        SessionFactory sessionFactory = (SessionFactory) EasyMock.createStrictMock(SessionFactory.class);
        EasyMock.expect(sessionFactory.openSession()).andReturn(session);
        PreviousEntriesDatabaseFetcher fetcher = new PreviousEntriesDatabaseFetcher(sessionFactory);

        EasyMock.replay(new Object[]{sessionFactory, session, connection, transaction, query});
        try {
            assertEquals(numPreviousEntries, fetcher.loadPreviousEntries(createBlog(11), new DatabaseEntry(), numPreviousEntries).length);
        } catch (FetcherException fe) {
            fail("FetcherException shouldn't have been thrown.");
        }
        EasyMock.verify(new Object[]{sessionFactory, session, connection, transaction, query});
    }

    public void testLoadPreviousEntriesHandlesHibernateException() {
        int numPreviousEntries = 10;
        SessionFactory sessionFactory = (SessionFactory) EasyMock.createStrictMock(SessionFactory.class);
        EasyMock.expect(sessionFactory.openSession()).andThrow(new HibernateException("Problem while retrieving entries."));
        PreviousEntriesDatabaseFetcher fetcher = new PreviousEntriesDatabaseFetcher(sessionFactory);

        EasyMock.replay(new Object[]{sessionFactory});
        try {
            assertEquals(numPreviousEntries, fetcher.loadPreviousEntries(createBlog(11), new DatabaseEntry(), numPreviousEntries).length);
            fail("Exception should've been thrown.");
        } catch (FetcherException fe) {
            // expected
        }
        EasyMock.verify(new Object[]{sessionFactory});
    }

    DatabaseBlog createBlog(int id) {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setId(new Integer(id));
        return blog;
    }

    List createDatabaseEntry(int numOfEntries) {
        List list = new ArrayList();
        for (int i = 0; i < numOfEntries; i++) {
            list.add(new DatabaseEntry());
        }
        return list;
    }
}
