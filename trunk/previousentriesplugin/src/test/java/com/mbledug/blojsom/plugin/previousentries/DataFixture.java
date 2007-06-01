package com.mbledug.blojsom.plugin.previousentries;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseEntry;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    PreviousEntriesDatabaseFetcher createMockFetcher(Exception e) {
        Mock mockFetcher = mock(
                PreviousEntriesDatabaseFetcher.class,
                new Class[]{SessionFactory.class},
                new Object[]{createMockSessionFactory(1)});
        mockFetcher.expects(once()).method("loadPreviousEntries").will(throwException(e));
        return (PreviousEntriesDatabaseFetcher) mockFetcher.proxy();
    }

    PreviousEntriesDatabaseFetcher createMockFetcher(int numPreviousEntries) {
        Mock mockFetcher = mock(
                PreviousEntriesDatabaseFetcher.class,
                new Class[]{SessionFactory.class},
                new Object[]{createMockSessionFactory(numPreviousEntries)});
        mockFetcher.expects(once()).method("loadPreviousEntries").will(returnValue(createDatabaseEntry(numPreviousEntries)));
        return (PreviousEntriesDatabaseFetcher) mockFetcher.proxy();
    }

    SessionFactory createMockSessionFactory(Exception e) {
        Mock mockSessionFactory = mock(SessionFactory.class);
        mockSessionFactory.expects(once()).method("openSession").will(throwException(e));
        return (SessionFactory) mockSessionFactory.proxy();
    }

    SessionFactory createMockSessionFactory(int numPreviousEntries) {
        Mock mockQuery = mock(Query.class);
        mockQuery.expects(once()).method("setInteger");
        mockQuery.expects(once()).method("setTimestamp");
        mockQuery.expects(once()).method("setFirstResult");
        mockQuery.expects(once()).method("setMaxResults");
        mockQuery.expects(once()).method("list").will(returnValue(createDatabaseEntry(numPreviousEntries)));
        Query query = (Query) mockQuery.proxy();

        Mock mockTransaction = mock(Transaction.class);
        mockTransaction.expects(once()).method("commit");
        Transaction transaction = (Transaction) mockTransaction.proxy();

        Mock mockSession = mock(Session.class);
        mockSession.expects(once()).method("beginTransaction").will(returnValue(transaction));
        mockSession.expects(once()).method("createQuery").will(returnValue(query));
        mockSession.expects(once()).method("close");
        Session session = (Session) mockSession.proxy();

        Mock mockSessionFactory = mock(SessionFactory.class);
        mockSessionFactory.expects(once()).method("openSession").will(returnValue(session));
        return (SessionFactory) mockSessionFactory.proxy();
    }

    List createDatabaseEntry(int numOfEntries) {
        List list = new ArrayList();
        for (int i = 0; i < numOfEntries; i++) {
            list.add(new DatabaseEntry());
        }
        return list;
    }

    DatabaseBlog createBlog(int id) {
        DatabaseBlog blog = new DatabaseBlog();
        blog.setId(new Integer(id));
        return blog;
    }

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
