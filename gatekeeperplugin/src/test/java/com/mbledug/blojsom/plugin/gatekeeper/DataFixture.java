package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.blojsom.plugin.comment.CommentPlugin;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mbledug.blojsom.plugin.gatekeeper.provider.BlogQAProvider;
import com.mbledug.blojsom.plugin.gatekeeper.provider.BlojsomQAProvider;
import com.mbledug.blojsom.plugin.gatekeeper.provider.DatabaseBlojsomQAProvider;

public class DataFixture extends MockObjectTestCase {

    public JdbcTemplate createMockJdbcTemplate(QA questionAnswer) {
        Map row = new HashMap();
        row.put(DatabaseBlojsomQAProvider.COLUMN_QUESTION, questionAnswer.getQuestion());
        row.put(DatabaseBlojsomQAProvider.COLUMN_ANSWER, questionAnswer.getAnswer());

        List result = new ArrayList();
        result.add(row);

        Mock mockJdbcTemplate = mock(JdbcTemplate.class);
        mockJdbcTemplate
                .expects(once())
                .method("queryForList")
                .will(returnValue(result));
        return (JdbcTemplate) mockJdbcTemplate.proxy();
    }

    HttpSession createMockHttpSessionWithSetAttribute() {
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession
                .expects(once())
                .method("setAttribute");;
        return (HttpSession) mockHttpSession.proxy();
    }

    HttpSession createMockHttpSessionWithGetAttribute(String answer) {
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession
                .expects(once())
                .method("getAttribute")
                .will(returnValue(new QA("dummy question", answer)));
        return (HttpSession) mockHttpSession.proxy();
    }

    HttpServletRequest createMockHttpServletRequestWithCommentFormSubmission(
            final String commentFormValue,
            final String inputGateKeeper,
            final String answerGateKeeper) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(CommentPlugin.COMMENT_PARAM))
                .will(returnValue(commentFormValue));
        mockHttpServletRequest
                .expects(once())
                .method("getSession")
                .will(returnValue(createMockHttpSessionWithGetAttribute(answerGateKeeper)));
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(GateKeeperPlugin.PARAM_GATEKEEPER))
                .will(returnValue(inputGateKeeper));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletRequest createMockHttpServletRequestWithNonCommentFormSubmission(
            final String commentFormValue) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(CommentPlugin.COMMENT_PARAM))
                .will(returnValue(commentFormValue));
        mockHttpServletRequest
                .expects(once())
                .method("getSession")
                .will(returnValue(createMockHttpSessionWithSetAttribute()));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletRequest createMockHttpServletRequest(
            final String commentFormValue) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(CommentPlugin.COMMENT_PARAM))
                .will(returnValue(commentFormValue));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }

    QAManager createQAManager() {
        return new QAManager(
                createBlojsomQAProviders(2, 20),
                createBlogQAProviders(5, 30));
    }

    List createBlogQAProviders(int numOfProviders, int numOfQuestionAnswer) {
        List providers = new ArrayList();
        for (int i = 0; i < numOfProviders; i++) {
            providers.add(createBlogQAProvider(numOfQuestionAnswer));
        }
        return providers;
    }

    BlogQAProvider createBlogQAProvider(int numOfQuestionAnswer) {
        Mock mockBlogQAProvider = mock(BlogQAProvider.class);
        mockBlogQAProvider
                .expects(once())
                .method("getQuestionAnswerList")
                .will(returnValue(createQuestionAnswerList(numOfQuestionAnswer)));
        return (BlogQAProvider) mockBlogQAProvider.proxy();
    }

    List createBlojsomQAProviders(int numOfProviders, int numOfQuestionAnswer) {
        List providers = new ArrayList();
        for (int i = 0; i < numOfProviders; i++) {
            providers.add(createBlojsomQAProvider(numOfQuestionAnswer));
        }
        return providers;
    }

    BlojsomQAProvider createBlojsomQAProvider(int numOfQuestionAnswer) {
        Mock mockBlojsomQAProvider = mock(BlojsomQAProvider.class);
        mockBlojsomQAProvider
                .expects(once())
                .method("getQuestionAnswerList")
                .will(returnValue(createQuestionAnswerList(numOfQuestionAnswer)));
        return (BlojsomQAProvider) mockBlojsomQAProvider.proxy();
    }

    List createQuestionAnswerList(int numOfQuestionAnswer) {
        List list = new ArrayList();
        for (int i = 0; i < numOfQuestionAnswer; i++) {
            list.add(new QA("dummy question", "dummy answer"));
        }
        return list;
    }
}
