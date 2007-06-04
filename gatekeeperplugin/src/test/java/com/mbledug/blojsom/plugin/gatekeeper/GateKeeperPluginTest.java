package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.comment.CommentPlugin;

public class GateKeeperPluginTest extends TestCase {

    private DataFixture mDataFixture;

    protected void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testCreatingNewSCodePluginWithNullImageFactoryIllegalArgumentException() {
        try {
            new GateKeeperPlugin(null);
            fail("Shouldn't have been able to construct GateKeeperPlugin with null QAManager.");
        } catch (IllegalArgumentException iae) {
            // expected IllegalArgumentException
        }
    }

    public void testProcessWithPluginDisabledDoesntMarkCommentForDeletion() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.FALSE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(mDataFixture.createMockHttpServletRequest("y"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithBlogCommentDisabledDoesntMarkCommentForDeletion() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.FALSE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(mDataFixture.createMockHttpServletRequest("y"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNonCommentFormSubmissionDoesntMarkCommentForDeletionAndPutQAIntoContext() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(mDataFixture.createMockHttpServletRequestWithNonCommentFormSubmission(null),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithCommentFormSubmissionHavingNullSCodeInputMarksCommentForDeletion() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            mDataFixture.createMockHttpServletRequestWithCommentFormSubmission("y", null, "eggtart"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNullSCodeInputMarksCommentForDeletionForContextAlreadyContainingCommentMetaData() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            context.put(CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA, new HashMap());
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            mDataFixture.createMockHttpServletRequestWithCommentFormSubmission("y", null, "eggtart"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithNullSCodeAnswerMarksCommentForDeletion() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            mDataFixture.createMockHttpServletRequestWithCommentFormSubmission("y", "eggtart", null),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithSCodeInputNotMatchingSCodeAnswerMarksCommentForDeletion() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            mDataFixture.createMockHttpServletRequestWithCommentFormSubmission("y", "eggtart", "fruittart"),
                            mDataFixture.createMockHttpServletResponse(),
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
    }

    public void testProcessWithSCodeInputMatchingSCodeAnswerDoesntMarkCommentForDeletion() {
        GateKeeperPlugin gateKeeperPlugin = new GateKeeperPlugin(mDataFixture.createQAManager());
        try {
            Map properties = new HashMap();
            properties.put(GateKeeperPlugin.PROPERTY_ENABLED, Boolean.TRUE.toString());
            Blog blog = new DatabaseBlog();
            blog.setProperties(properties);
            blog.setBlogCommentsEnabled(Boolean.TRUE);
            Map context = new HashMap();
            gateKeeperPlugin.init();
            gateKeeperPlugin.process(
                            mDataFixture.createMockHttpServletRequestWithCommentFormSubmission("y", "eggtart", "eggtart"),
                            mDataFixture.createMockHttpServletResponse(),
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
