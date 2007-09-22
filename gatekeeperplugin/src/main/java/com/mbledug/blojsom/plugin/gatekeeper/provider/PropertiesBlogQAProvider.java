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
 *   * Neither the name of Qoqoa nor the names of its contributors
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
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;

import com.mbledug.blojsom.plugin.gatekeeper.QA;

/**
 * Provides a list of question answer from blog properties.
 * <p>
 * A value will be passed to the constructor, which will then determine the
 * property names within the blog.
 * </p>
 * <p>
 * E.g. if this value is set to 20, then the property names would be:
 * </p>
 * <ul>
 * <li>gatekeeper-question-1 and gatekeeper-answer-1</li>
 * <li>gatekeeper-question-2 and gatekeeper-answer-2</li>
 * <li>...</li>
 * <li>gatekeeper-question-20 and gatekeeper-answer-20</li>
 * </ul>
 * <p>
 * Only the property number with existing pair of question and answer will then
 * be passed on as {@link QA} object.
 * </p>
 * <p>
 * A warning message will be displayed for property number with incomplete
 * properties (i.e. question without number).
 * </p>
 * <p>
 * Property number without both question and number will be ignored.
 * </p>
 * @author Cliffano Subagio
 */
public class PropertiesBlogQAProvider implements BlogQAProvider {

    /**
     * Logger for {@link PropertiesBlogQAProvider}.
     */
    private static final Log LOG = LogFactory.getLog(
            PropertiesBlogQAProvider.class);

    /**
     * Prefix name used to identify question and answer properties.
     */
    private static final String PROPERTY_PREFIX = "gatekeeper-";

    /**
     * Question property name, will appended after the prefix.
     */
    private static final String PROPERTY_QUESTION = "question-";

    /**
     * Answer property name, will appended after the prefix.
     */
    private static final String PROPERTY_ANSWER = "answer-";

    /**
     * The number of question answer will be retrieved from blog properties.
     */
    private int mNumOfProperties;

    /**
     * Constructs {@link PropertiesBlogQAProvider}.
     * @param numOfProperties the number of question answer properties, this
     * value must be greater than or equal to 1
     */
    public PropertiesBlogQAProvider(final int numOfProperties) {
        if (numOfProperties < 1) {
            throw new IllegalArgumentException("Number of question answer "
                    + " properties must be greater than or equal to 1.");
        }
        mNumOfProperties = numOfProperties;
    }

    /**
     * {@inheritDoc}
     */
    public final List getQuestionAnswerList(final Blog blog) {
        List questionAnswerList = new ArrayList();

        Map properties = blog.getProperties();
        for (int i = 1; i <= mNumOfProperties; i++) {
            String questionPropertyName =
                    PROPERTY_PREFIX + PROPERTY_QUESTION + i;
            Object question = properties.get(questionPropertyName);
            if (question != null) {
                String answerPropertyName =
                        PROPERTY_PREFIX + PROPERTY_ANSWER + i;
                Object answer = properties.get(answerPropertyName);
                if (answer == null) {
                    LOG.warn("Question with property name: "
                            + questionPropertyName
                            + ", does not have a corresponding answer with "
                            + "property name: " + answerPropertyName);
                } else {
                    QA questionAnswer = new QA(
                            String.valueOf(question),
                            String.valueOf(answer));
                    questionAnswerList.add(questionAnswer);
                }
            }
        }

        return questionAnswerList;
    }
}
