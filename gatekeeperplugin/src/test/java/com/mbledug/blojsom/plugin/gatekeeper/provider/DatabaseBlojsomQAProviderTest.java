package com.mbledug.blojsom.plugin.gatekeeper.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mbledug.blojsom.plugin.gatekeeper.QA;

public class DatabaseBlojsomQAProviderTest extends TestCase {

    public void testGetQuestionAnswerListReturnsResult() {
        String expectedQuestion = "dummy question";
        String expectedAnswer = "dummy answer";
        QA expectedQuestionAnswer = new QA(expectedQuestion, expectedAnswer);

        Map row = new HashMap();
        row.put(DatabaseBlojsomQAProvider.COLUMN_QUESTION, expectedQuestionAnswer.getQuestion());
        row.put(DatabaseBlojsomQAProvider.COLUMN_ANSWER, expectedQuestionAnswer.getAnswer());

        List result = new ArrayList();
        result.add(row);

        JdbcTemplate jdbcTemplate = (JdbcTemplate) EasyMock.createStrictMock(JdbcTemplate.class);
        EasyMock.expect(jdbcTemplate.queryForList("select * from gatekeeper")).andReturn(result);

        EasyMock.replay(new Object[]{jdbcTemplate});

        DatabaseBlojsomQAProvider databaseBlojsomQAProvider = new DatabaseBlojsomQAProvider(jdbcTemplate);
        List questionAnswerList = databaseBlojsomQAProvider.getQuestionAnswerList();
        assertTrue(questionAnswerList.size() >= 1);

        QA retrievedQuestionAnswer = (QA) questionAnswerList.get(0);
        assertEquals(expectedQuestionAnswer.getQuestion(), retrievedQuestionAnswer.getQuestion());
        assertEquals(expectedQuestionAnswer.getAnswer(), retrievedQuestionAnswer.getAnswer());

        EasyMock.verify(new Object[]{jdbcTemplate});
    }
}
