package com.mbledug.blojsom.plugin.scode;

import java.util.HashMap;
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
import org.easymock.classextension.EasyMock;

import com.mbledug.blojsom.plugin.scode.engine.FishEyeImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.FunkyImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.GradientImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.KinkImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.ShadowImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.SimpleImageEngine;

public class SCodePluginTest extends TestCase {

    public void testCreatingNewSCodePluginWithNullImageFactoryIllegalArgumentException() {
        try {
            SCodePlugin sCodePlugin = new SCodePlugin(null);
            fail("Shouldn't have been able to construct SCodePlugin with null image factory. SCodePlugin: " + sCodePlugin);
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    public void testProcessWithPluginDisabledDoesntMarkCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(null);
        session.setAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY, imageFactory);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});

        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.FALSE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Disabled plugin shouldn't mark comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithBlogCommentDisabledDoesntMarkCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(null);
        session.setAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY, imageFactory);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});

        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.FALSE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Blog with disabled comment shouldn't mark comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithNonCommentFormSubmissionDoesntMarkCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(null);
        session.setAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY, imageFactory);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn(null);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});

        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Input from non comment form submission shouldn't mark comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithNullSCodeInputMarksCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(imageFactory);
        EasyMock.expect(session.getAttribute(SCodePlugin.PARAM_SCODE)).andReturn("888888");
        session.removeAttribute(SCodePlugin.PARAM_SCODE);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(SCodePlugin.PARAM_SCODE)).andReturn(null);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});

        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with null SCode input should've marked comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithNullSCodeInputMarksCommentForDeletionForContextAlreadyContainingCommentMetaData() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(imageFactory);
        EasyMock.expect(session.getAttribute(SCodePlugin.PARAM_SCODE)).andReturn("888888");
        session.removeAttribute(SCodePlugin.PARAM_SCODE);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(SCodePlugin.PARAM_SCODE)).andReturn(null);
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});

        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            context.put(CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA, new HashMap());
            sCodePlugin.init();
            sCodePlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with null SCode input should've marked comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithNullSCodeAnswerMarksCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(imageFactory);
        EasyMock.expect(session.getAttribute(SCodePlugin.PARAM_SCODE)).andReturn(null);
        session.removeAttribute(SCodePlugin.PARAM_SCODE);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(SCodePlugin.PARAM_SCODE)).andReturn("888888");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});

        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with null SCode answer should've marked comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithSCodeInputNotMatchingSCodeAnswerMarksCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(imageFactory);
        EasyMock.expect(session.getAttribute(SCodePlugin.PARAM_SCODE)).andReturn("777777");
        session.removeAttribute(SCodePlugin.PARAM_SCODE);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(SCodePlugin.PARAM_SCODE)).andReturn("888888");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (!isMarkedForDeletion(context)) {
                fail("Process with SCode input not matching SCode answer should've marked comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
    }

    public void testProcessWithSCodeInputMatchingSCodeAnswerDoesntMarkCommentForDeletion() {
        ImageFactory imageFactory = new ImageFactory(createEngines());
		SCodePlugin sCodePlugin = new SCodePlugin(imageFactory);

        HttpSession session = (HttpSession) EasyMock.createStrictMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY)).andReturn(imageFactory);
        EasyMock.expect(session.getAttribute(SCodePlugin.PARAM_SCODE)).andReturn("777777");
        session.removeAttribute(SCodePlugin.PARAM_SCODE);
        HttpServletRequest request = (HttpServletRequest) EasyMock.createStrictMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.expect(request.getParameter(CommentPlugin.COMMENT_PARAM)).andReturn("y");
        EasyMock.expect(request.getParameter(SCodePlugin.PARAM_SCODE)).andReturn("777777");
        HttpServletResponse response = (HttpServletResponse) EasyMock.createStrictMock(HttpServletResponse.class);

        EasyMock.replay(new Object[]{session, request, response});
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            request,
                            response,
                            blog,
                            context,
                            new Entry[]{});
            if (isMarkedForDeletion(context)) {
                fail("Process with SCode input matching SCode answer shouldn't mark comment for deletion.");
            }
            sCodePlugin.cleanup();
            sCodePlugin.destroy();
        } catch (PluginException pe) {
            fail("Unexpected PluginException thrown: " + pe);
        }

        EasyMock.verify(new Object[]{session, request, response});
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

    private Map createEngines() {
        Map engines = new HashMap();
        engines.put("simple", new SimpleImageEngine());
        engines.put("gradient", new GradientImageEngine());
        engines.put("funky", new FunkyImageEngine());
        engines.put("kink", new KinkImageEngine());
        engines.put("fisheye", new FishEyeImageEngine());
        engines.put("shadow", new ShadowImageEngine());
        return engines;
    }
}
