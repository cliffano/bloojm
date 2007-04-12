package com.mbledug.blojsom.plugin.scode;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.blojsom.plugin.comment.CommentPlugin;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import com.mbledug.blojsom.plugin.scode.engine.FunkyImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.GradientImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.KinkImageEngine;
import com.mbledug.blojsom.plugin.scode.engine.SimpleImageEngine;

public class DataFixture extends MockObjectTestCase {

    static final String SCODE_TEXT = "456789";

    static Map createEngines() {
        Map engines = new HashMap();
        engines.put("simple", new SimpleImageEngine());
        engines.put("gradient", new GradientImageEngine());
        engines.put("funky", new FunkyImageEngine());
        engines.put("kink", new KinkImageEngine());
        return engines;
    }

    HttpSession createMockHttpSessionWithoutSCodeCheck() {
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession
                .expects(once())
                .method("getAttribute")
                .with(eq(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY))
                .will(returnValue(null));
        mockHttpSession
                .expects(once())
                .method("setAttribute");
        return (HttpSession) mockHttpSession.proxy();
    }

    HttpSession createMockHttpSessionWithSCodeCheck(
            final String answerSCode) {
        Mock mockHttpSession = mock(HttpSession.class);
        mockHttpSession
                .expects(once())
                .method("getAttribute")
                .with(eq(SCodePlugin.SESSION_ATTR_IMAGE_FACTORY))
                .will(returnValue(new ImageFactory(createEngines())));
        mockHttpSession
                .expects(once())
                .method("getAttribute")
                .with(eq(SCodePlugin.PARAM_SCODE))
                .will(returnValue(answerSCode));
        mockHttpSession
                .expects(once())
                .method("removeAttribute")
                .with(eq(SCodePlugin.PARAM_SCODE));
        return (HttpSession) mockHttpSession.proxy();
    }

    HttpServletRequest createMockHttpServletRequestWithoutSCodeCheck(
            final String commentFormValue) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getSession")
                .will(returnValue(createMockHttpSessionWithoutSCodeCheck()));
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(CommentPlugin.COMMENT_PARAM))
                .will(returnValue(commentFormValue));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletRequest createMockHttpServletRequestWithSCodeCheck(
            final String commentFormValue,
            final String inputSCode,
            final String answerSCode) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getSession")
                .will(returnValue(createMockHttpSessionWithSCodeCheck(answerSCode)));
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(CommentPlugin.COMMENT_PARAM))
                .will(returnValue(commentFormValue));
        mockHttpServletRequest
                .expects(once())
                .method("getParameter")
                .with(eq(SCodePlugin.PARAM_SCODE))
                .will(returnValue(inputSCode));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
