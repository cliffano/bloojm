package com.mbledug.blojsom.plugin.param2ctx;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    static final String[] PARAM_NAMES = {"paramName1", "paramName2"};
    static final String[] PARAM_VALUES = {"paramValue1", "paramValue2"};

    Map createContextWithParams() {
        Map context = new HashMap();
        context.put("dummyKey1", "dummyValue1");
        context.put("dummyKey2", "dummyValue2");
        return context;
    }

    HttpServletRequest createMockHttpServletRequestWithParams() {

        Mock mockRequest = new Mock(HttpServletRequest.class);

        mockRequest.expects(once()).method("getParameter")
                .with(eq(PARAM_NAMES[0])).will(returnValue(PARAM_NAMES[0]));
        mockRequest.expects(once()).method("getParameter")
                .with(eq(PARAM_NAMES[1])).will(returnValue(PARAM_NAMES[1]));

        Vector paramNames = new Vector();
        paramNames.add(PARAM_NAMES[0]);
        paramNames.add(PARAM_NAMES[1]);
        mockRequest.expects(once()).method("getParameterNames")
                .withNoArguments().will(returnValue(paramNames.elements()));

        Map paramsMap = new HashMap();
        paramsMap.put(PARAM_NAMES[0], PARAM_VALUES[0]);
        paramsMap.put(PARAM_NAMES[1], PARAM_VALUES[1]);
        mockRequest.expects(once()).method("getParameterMap")
                .withNoArguments().will(returnValue(paramsMap));

        return (HttpServletRequest) mockRequest.proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}
