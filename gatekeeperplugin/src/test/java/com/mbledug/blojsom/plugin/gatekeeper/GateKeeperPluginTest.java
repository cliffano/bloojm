package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.CommentPlugin;
import org.easymock.EasyMock;

import com.mbledug.blojsom.plugin.gatekeeper.provider.BlogQAProvider;
import com.mbledug.blojsom.plugin.gatekeeper.provider.BlojsomQAProvider;

public class GateKeeperPluginTest extends TestCase {

    public void testCreatingNewSCodePluginWithNullImageFactoryIllegalArgumentException() {
        try {
            new GateKeeperPlugin(null);
            fail("Shouldn't have been able to construct GateKeeperPlugin with null QAManager.");
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    public void testProcessWithPluginDisabledDoesntMarkCommentForDeletion() {

    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.FALSE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Disabled plugin shouldn't mark comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response});
    }

    public void testProcessWithBlogCommentDisabledDoesntMarkCommentForDeletion() {

    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.FALSE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Blog with disabled comment shouldn't mark comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response});
    }

    public void testProcessWithNonCommentFormSubmissionDoesntMarkCommentForDeletionAndPutsQAIntoContext() {
    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAs = createQuestionAnswerList(1);
		EasyMock.expect(blogQAProvider.getQuestionAnswerList(blog)).andReturn(blogQAs);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	EasyMock.expect(blojsomQAProvider.getQuestionAnswerList()).andReturn(blogQAs);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        session.setAttribute(GateKeeperPlugin.SESSION_ATTR_QA, blogQAs.get(0));

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn(null);
        EasyMock.expect(request.getSession()).andReturn(session);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Input from non comment form submission shouldn't mark comment for deletion.");
            }
            assertNotNull(context.get(GateKeeperPlugin.CONTEXT_QA));
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
    }

    public void testProcessWithCommentFormSubmissionHavingNullSCodeInputMarksCommentForDeletion() {
    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAs = createQuestionAnswerList(1);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GateKeeperPlugin.SESSION_ATTR_QA)).andReturn(new QA("dummy question", "eggtart"));

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(GateKeeperPlugin.PARAM_GATEKEEPER)).andReturn(null);
        EasyMock.expect(request.getSession()).andReturn(session);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with null GateKeeper input should've marked comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
    }

    public void testProcessWithNullSCodeInputMarksCommentForDeletionForContextAlreadyContainingCommentMetaData() {
    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAs = createQuestionAnswerList(1);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GateKeeperPlugin.SESSION_ATTR_QA)).andReturn(new QA("dummy question", "eggtart"));

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(GateKeeperPlugin.PARAM_GATEKEEPER)).andReturn(null);
        EasyMock.expect(request.getSession()).andReturn(session);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            context.put(CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA, new HashMap());
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with null GateKeeper input should've marked comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
    }

    public void testProcessWithNullSCodeAnswerMarksCommentForDeletion() {
    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAs = createQuestionAnswerList(1);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GateKeeperPlugin.SESSION_ATTR_QA)).andReturn(new QA("dummy question", null));

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(GateKeeperPlugin.PARAM_GATEKEEPER)).andReturn("eggtart");
        EasyMock.expect(request.getSession()).andReturn(session);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with null SCode answer should've marked comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }
        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
    }

    public void testProcessWithSCodeInputNotMatchingSCodeAnswerMarksCommentForDeletion() {
    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAs = createQuestionAnswerList(1);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GateKeeperPlugin.SESSION_ATTR_QA)).andReturn(new QA("dummy question", "fruittart"));

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(GateKeeperPlugin.PARAM_GATEKEEPER)).andReturn("eggtart");
        EasyMock.expect(request.getSession()).andReturn(session);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});

        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with SCode input not matching SCode answer should've marked comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }
        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
    }

    public void testProcessWithSCodeInputMatchingSCodeAnswerDoesntMarkCommentForDeletion() {
    	Blog blog = new DatabaseBlog();

    	BlogQAProvider blogQAProvider = (BlogQAProvider) EasyMock.createStrictMock(BlogQAProvider.class);
    	List blogQAs = createQuestionAnswerList(1);
    	List blogQAProviders = new ArrayList();
    	blogQAProviders.add(blogQAProvider);

    	BlojsomQAProvider blojsomQAProvider = (BlojsomQAProvider) EasyMock.createStrictMock(BlojsomQAProvider.class);
    	List blojsomQAProviders = new ArrayList();
    	blojsomQAProviders.add(blojsomQAProvider);


        QAManager manager = new QAManager(
        		blojsomQAProviders,
                blogQAProviders);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GateKeeperPlugin.SESSION_ATTR_QA)).andReturn(new QA("dummy question", "eggtart"));

        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(GateKeeperPlugin.PARAM_GATEKEEPER)).andReturn("eggtart");
        EasyMock.expect(request.getSession()).andReturn(session);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(manager);
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Process with SCode input matching SCode answer shouldn't mark comment for deletion.");
            }
            gateKeeperPlugin.cleanup();
            gateKeeperPlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }
        EasyMock.verify(new Object[]{blogQAProvider, blojsomQAProvider, request, response, session});
    }

    private boolean isMarkedForDeletion(final Map context) {
        boolean isMarkedForDeletion;

        Map commentMetaData = (Map) context.get(
                CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA);
        if (commentMetaData != null && Boolean.TRUE.toString().equals(commentMetaData.get(
                CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA_DESTROY))) {
            isMarkedForDeletion = true;
        } else {
            isMarkedForDeletion = false;
        }
        return isMarkedForDeletion;
    }

    private List createQuestionAnswerList(int numOfQuestionAnswer) {
        List list = new ArrayList();
        for (int i = 0; i < numOfQuestionAnswer; i++) {
            list.add(new QA("dummy question", "dummy answer"));
        }
        return list;
    }
}
