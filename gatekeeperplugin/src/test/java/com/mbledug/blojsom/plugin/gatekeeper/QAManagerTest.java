package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.blojsom.blog.database.DatabaseBlog;

public class QAManagerTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testGetRandomQuestionAnswer() {
        QAManager manager = new QAManager(
                mDataFixture.createBlojsomQAProviders(3, 20),
                mDataFixture.createBlogQAProviders(5, 60));
        QA questionAnswer = manager.getRandomQuestionAnswer(new DatabaseBlog());
        assertNotNull(questionAnswer);
    }

    public void testConstructQAManagerWithNullBlojsomQAProvidersThrowsIllegalArgumentException() {
        try {
            new QAManager(null, new ArrayList());
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructQAManagerWithNullBlogQAProvidersThrowsIllegalArgumentException() {
        try {
            new QAManager(new ArrayList(), null);
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructQAManagerWithZeroQAProvidersThrowsIllegalArgumentException() {
        try {
            new QAManager(new ArrayList(), new ArrayList());
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }
}
