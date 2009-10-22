/**
 * Copyright (c) 2007, Cliffano Subagio
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of Studio Cliffano nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mbledug.blojsom.plugin.previousentries;

import java.util.List;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.fetcher.FetcherException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * {@link PreviousEntriesDatabaseFetcher} fetches previous entries from the
 * database.
 * @author Cliffano
 */
public class PreviousEntriesDatabaseFetcher {

    /**
     * The HQL query used for loading previous entries.
     */
    private static final String QUERY = "from DatabaseEntry "
            + "where blog_id = :blogId and entry_date <= :entryDate "
            + "order by entry_date desc";

    /**
     * Session factory that creates Hibernate session.
     */
    private SessionFactory mSessionFactory;

    /**
     * Creates {@link PreviousEntriesDatabaseFetcher} with specified session
     * factory.
     * @param sessionFactory the session factory
     */
    public PreviousEntriesDatabaseFetcher(final SessionFactory sessionFactory) {
        mSessionFactory = sessionFactory;
    }

    /**
     * Executes the HQL query to retrieve the previous entries.
     * @param blog the current blog
     * @param entry the current entry
     * @param numPreviousEntries number of previous entries to retrieve
     * @return an array of previous entries
     * @throws FetcherException when there's a problem with retrieving the
     *         previous entries from the database
     */
    // checkstyle suggests this method to be final,
    // but EasyMock can't mock final method
    public Entry[] loadPreviousEntries(
            final Blog blog,
            final Entry entry,
            final int numPreviousEntries) throws FetcherException {

        Entry[] previousEntries;
        Session session = null;
        try {
            session = mSessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery(QUERY);
            query.setInteger("blogId", blog.getId().intValue());
            query.setTimestamp("entryDate", entry.getDate());
            query.setFirstResult(1);
            query.setMaxResults(numPreviousEntries);
            List resultList = query.list();
            previousEntries = (Entry[]) resultList.toArray(
                    new DatabaseEntry[resultList.size()]);

            tx.commit();
        } catch (HibernateException he) {
            throw new FetcherException(he);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return previousEntries;

    }
}
