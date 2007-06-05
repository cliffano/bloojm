package com.mbledug.blojsom.plugin.gatekeeper.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.blojsom.blog.Blog;
import org.blojsom.blog.database.DatabaseBlog;

import com.mbledug.blojsom.plugin.gatekeeper.QA;

public class PropertiesBlogQAProviderTest extends TestCase {

    public void testConstructorWithLessThanOneNumOfPropertiesThrowsIllegalArgumentException() {
        try {
            new PropertiesBlogQAProvider(0);
            fail("IllegalArgumentException should've been thrown.");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testGetQuestionAnswerListWithFirstAndLastPropertyNumbers() {
        String firstQuestion = "dummy question 1";
        String firstAnswer = "dummy answer 1";
        String lastQuestion = "dummy question 20";
        String lastAnswer = "dummy answer 20";

        Map properties = new HashMap();
        properties.put("gatekeeper-question-1", firstQuestion);
        properties.put("gatekeeper-answer-1", firstAnswer);
        properties.put("gatekeeper-question-20", lastQuestion);
        properties.put("gatekeeper-answer-20", lastAnswer);

        Blog blog = new DatabaseBlog();
        blog.setProperties(properties);

        PropertiesBlogQAProvider provider = new PropertiesBlogQAProvider(20);
        List questionAnswerList = provider.getQuestionAnswerList(blog);

        QA firstQuestionAnswer = (QA) questionAnswerList.get(0);
        QA lastQuestionAnswer = (QA) questionAnswerList.get(1);

        assertEquals(firstQuestion, firstQuestionAnswer.getQuestion());
        assertEquals(firstAnswer, firstQuestionAnswer.getAnswer());
        assertEquals(lastQuestion, lastQuestionAnswer.getQuestion());
        assertEquals(lastAnswer, lastQuestionAnswer.getAnswer());
    }

    public void testGetQuestionAnswerListHavingQuestionsWithoutAnswersWontBeAddedAsPropertiesToList() {
        Map properties = new HashMap();
        properties.put("gatekeeper-question-1", "dummy question 1");
        properties.put("gatekeeper-question-2", "dummy question 2");

        Blog blog = new DatabaseBlog();
        blog.setProperties(properties);

        PropertiesBlogQAProvider provider = new PropertiesBlogQAProvider(20);
        List questionAnswerList = provider.getQuestionAnswerList(blog);

        assertTrue(questionAnswerList.size() == 0);
    }

    public void testGetQuestionAnswerListHavingAnswersWithoutQuestionsWontBeAddedAsPropertiesToList() {
        Map properties = new HashMap();
        properties.put("gatekeeper-answer-1", "dummy answer 1");
        properties.put("gatekeeper-answer-2", "dummy answer 2");

        Blog blog = new DatabaseBlog();
        blog.setProperties(properties);

        PropertiesBlogQAProvider provider = new PropertiesBlogQAProvider(20);
        List questionAnswerList = provider.getQuestionAnswerList(blog);

        assertTrue(questionAnswerList.size() == 0);
    }

    public void testGetQuestionAnswerListHavingPropertyNumbersOutsideTheBoundaryWontBeAddedAsPropertiesToList() {
        String firstQuestion = "dummy question 100";
        String firstAnswer = "dummy answer 100";
        String lastQuestion = "dummy question 2000";
        String lastAnswer = "dummy answer 2000";

        Map properties = new HashMap();
        properties.put("gatekeeper-question-100", firstQuestion);
        properties.put("gatekeeper-answer-100", firstAnswer);
        properties.put("gatekeeper-question-2000", lastQuestion);
        properties.put("gatekeeper-answer-2000", lastAnswer);

        Blog blog = new DatabaseBlog();
        blog.setProperties(properties);

        PropertiesBlogQAProvider provider = new PropertiesBlogQAProvider(20);
        List questionAnswerList = provider.getQuestionAnswerList(blog);

        assertTrue(questionAnswerList.size() == 0);
    }
}
