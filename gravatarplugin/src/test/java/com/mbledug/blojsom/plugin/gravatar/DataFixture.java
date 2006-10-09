package com.mbledug.blojsom.plugin.gravatar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Entry;
import org.blojsom.blog.database.DatabaseBlog;
import org.blojsom.blog.database.DatabaseComment;
import org.blojsom.blog.database.DatabaseEntry;
import org.blojsom.plugin.comment.event.CommentAddedEvent;
import org.blojsom.plugin.comment.event.CommentEvent;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    static final String EMAIL = "foo@bar.com";
    static final String EXPECTED_GRAVATAR_ID = "f3ada405ce890b6f8204094deb12d8a8";

    static Entry createEntryWithSingleCommentWithoutGravatarId() {

        List comments = new ArrayList();
        comments.add(createCommentWithoutGravatarId());
        DatabaseEntry entry = new DatabaseEntry();
        entry.setComments(comments);
        return entry;
    }

    static Entry createEntryWithSingleCommentWithEmptyStringGravatarId() {

        List comments = new ArrayList();
        comments.add(createCommentWithEmptyStringGravatarId());
        DatabaseEntry entry = new DatabaseEntry();
        entry.setComments(comments);
        return entry;
    }

    static CommentAddedEvent createCommentAddedEventWithCommentHavingNoGravatarId() {

        return new CommentAddedEvent(
                new Object(),
                new Date(),
                createCommentWithoutGravatarId(),
                new DatabaseBlog());
    }

    static CommentEvent createCommentEvent() {

        return new CommentEvent(
                new Object(),
                new Date(),
                new DatabaseComment(),
                new DatabaseBlog());
    }

    static DatabaseComment createCommentWithoutGravatarId() {

        DatabaseComment comment = new DatabaseComment();
        comment.setAuthorEmail(EMAIL);
        comment.setMetaData(new HashMap());
        return comment;
    }

    static DatabaseComment createCommentWithEmptyStringGravatarId() {

        Map metaData =  new HashMap();
        metaData.put(GravatarPlugin.METADATA_GRAVATAR_ID, "");
        DatabaseComment comment = new DatabaseComment();
        comment.setAuthorEmail(EMAIL);
        comment.setMetaData(metaData);
        return comment;
    }

    HttpServletRequest createMockHttpServletRequest() {
        return (HttpServletRequest) mock(HttpServletRequest.class).proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
