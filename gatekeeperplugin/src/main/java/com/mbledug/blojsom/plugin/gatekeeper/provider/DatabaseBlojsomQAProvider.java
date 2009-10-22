/**
 * Copyright (c) 2007, Cliffano Subagio
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of Studio Cliffano nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mbledug.blojsom.plugin.gatekeeper.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mbledug.blojsom.plugin.gatekeeper.QA;

/**
 * Provides a list of question answer retrieved from the database.
 * @author Cliffano Subagio
 */
public class DatabaseBlojsomQAProvider implements BlojsomQAProvider {

    /**
     * Column name for GateKeeper question.
     */
    public static final String COLUMN_QUESTION = "question";

    /**
     * Column name for GateKeeper answer.
     */
    public static final String COLUMN_ANSWER = "answer";

    /**
     * JdbcTemplate used to execute the SQL query.
     */
    private JdbcTemplate mJdbcTemplate;

    /**
     * Constructs {@link IpToCountryDao} with specified JdbcTemplate.
     * @param jdbcTemplate the JdbcTemplate
     */
    DatabaseBlojsomQAProvider(final JdbcTemplate jdbcTemplate) {
        mJdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    public final List getQuestionAnswerList() {

        List questionAnswerList = new ArrayList();

        String sql = "select * from gatekeeper";

        List result = mJdbcTemplate.queryForList(sql);
        for (Iterator it = result.iterator(); it.hasNext();) {
            Map row = (Map) it.next();
            QA questionAnswer = new QA(
                    String.valueOf(row.get(COLUMN_QUESTION)),
                    String.valueOf(row.get(COLUMN_ANSWER)));
            questionAnswerList.add(questionAnswer);
        }

        return questionAnswerList;
    }
}
