package com.mbledug.blojsom.plugin.scode;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.CommentPlugin;

public class SCodePluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testCreatingNewSCodePluginWithNullImageFactoryIllegalArgumentException() {
        try {
            SCodePlugin sCodePlugin = new SCodePlugin(null);
            fail("Shouldn't have been able to construct SCodePlugin with null image factory. SCodePlugin: " + sCodePlugin);
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    public void testProcessWithPluginDisabledDoesntMarkCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.FALSE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(mDataFixture.createMockHttpServletRequestWithoutSCodeCheck("y"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithBlogCommentDisabledDoesntMarkCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.FALSE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(mDataFixture.createMockHttpServletRequestWithoutSCodeCheck("y"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNonCommentFormSubmissionDoesntMarkCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(mDataFixture.createMockHttpServletRequestWithoutSCodeCheck(null),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNullSCodeInputMarksCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            mDataFixture.createMockHttpServletRequestWithSCodeCheck("y", null, "888888"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNullSCodeInputMarksCommentForDeletionForContextAlreadyContainingCommentMetaData() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
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
                            mDataFixture.createMockHttpServletRequestWithSCodeCheck("y", null, "888888"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNullSCodeAnswerMarksCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            mDataFixture.createMockHttpServletRequestWithSCodeCheck("y", "888888", null),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithSCodeInputNotMatchingSCodeAnswerMarksCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            mDataFixture.createMockHttpServletRequestWithSCodeCheck("y", "888888", "777777"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithSCodeInputMatchingSCodeAnswerDoesntMarkCommentForDeletion() {
        SCodePlugin sCodePlugin = new SCodePlugin(new ImageFactory(DataFixture.createEngines()));
        try {
            Map properties = new HashMap();
            properties.put(SCodePlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            sCodePlugin.init();
            sCodePlugin.process(
                            mDataFixture.createMockHttpServletRequestWithSCodeCheck("y", "777777", "777777"),
                            mDataFixture.createMockHttpServletResponse(),
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
}
