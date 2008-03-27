package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.blojsom.blog.database.DatabaseBlog;
import org.easymock.EasyMock;

import com.mbledug.blojsom.plugin.gatekeeper.provider.BlogQAProvider;
import com.mbledug.blojsom.plugin.gatekeeper.provider.BlojsomQAProvider;

public class QAManagerTest extends TestCase {

    public void testGetRandomQuestionAnswer() {

    	DatabaseBlog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	EasyMock.expect(blogQAProvider.getQuestionAnswerList(blog)).andReturn(createQuestionAnswerList(1));
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	EasyMock.expect(blojsomQAProvider.getQuestionAnswerList()).andReturn(createQuestionAnswerList(1));
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);

    	EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider});

        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);
		QA questionAnswer = manager.getRandomQuestionAnswer(blog);
        assertNotNull(questionAnswer);

        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider});
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

    private List createQuestionAnswerList(int numOfQuestionAnswer) {
        List list = new ArrayList();
        for (int i = 0; i < numOfQuestionAnswer; i++) {
            list.add(new QA("dummy question", "dummy answer"));
        }
        return list;
    }
}
