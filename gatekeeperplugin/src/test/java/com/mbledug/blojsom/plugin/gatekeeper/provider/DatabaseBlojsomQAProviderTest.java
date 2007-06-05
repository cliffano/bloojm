package com.mbledug.blojsom.plugin.gatekeeper.provider;

import java.util.List;

import junit.framework.TestCase;

import com.mbledug.blojsom.plugin.gatekeeper.DataFixture;
import com.mbledug.blojsom.plugin.gatekeeper.QA;

public class DatabaseBlojsomQAProviderTest extends TestCase {

    DataFixture mDataFixture;

    public void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testGetQuestionAnswerListReturnsResult() {
        String expectedQuestion = "dummy question";
        String expectedAnswer = "dummy answer";
        QA expectedQuestionAnswer = new QA(expectedQuestion, expectedAnswer);

        DatabaseBlojsomQAProvider databaseBlojsomQAProvider = new DatabaseBlojsomQAProvider(mDataFixture.createMockJdbcTemplate(expectedQuestionAnswer));
        List questionAnswerList = databaseBlojsomQAProvider.getQuestionAnswerList();
        assertTrue(questionAnswerList.size() >= 1);

        QA retrievedQuestionAnswer = (QA) questionAnswerList.get(0);
        assertEquals(expectedQuestionAnswer.getQuestion(), retrievedQuestionAnswer.getQuestion());
        assertEquals(expectedQuestionAnswer.getAnswer(), retrievedQuestionAnswer.getAnswer());
    }
}
