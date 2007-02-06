package com.mbledug.blojsom.plugin.pager;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blojsom.blog.Blog;
import org.blojsom.blog.BlogEntry;
import org.blojsom.blog.BlogUser;
import org.blojsom.blog.BlojsomConfigurationException;
import org.blojsom.blog.FileBackedBlogEntry;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class DataFixture extends MockObjectTestCase {

    static BlogEntry[] createBlogEntries(int totalEntries) {
        BlogEntry[] blogEntries = new BlogEntry[totalEntries];
        for (int i = 0; i < blogEntries.length; i++) {
            blogEntries[i] = new FileBackedBlogEntry();
        }
        return blogEntries;
    }

    static BlogUser createBlogUserWithDisplayEntries(int displayEntries) {
        Blog blog = null;
        try {
            Properties properties = new Properties();
            properties.put("blog-home", "someDummyBlogHome");
            properties.put("blog-url", "someDummyBlogUrl");
            properties.put("blog-base-url", "someDummyBlogBaseUrl");
            blog = new Blog(properties);
        } catch (BlojsomConfigurationException bce) {

        }
        blog.setBlogDisplayEntries(displayEntries);
        BlogUser blogUser = new BlogUser();
        blogUser.setBlog(blog);
        return blogUser;
    }

    HttpServletRequest createMockHttpServletRequest(String pageNumParamValue) {
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest.expects(once()).method("getParameter").will(returnValue(pageNumParamValue));
        return (HttpServletRequest) mockHttpServletRequest.proxy();
    }

    HttpServletResponse createMockHttpServletResponse() {
        return (HttpServletResponse) mock(HttpServletResponse.class).proxy();
    }
}