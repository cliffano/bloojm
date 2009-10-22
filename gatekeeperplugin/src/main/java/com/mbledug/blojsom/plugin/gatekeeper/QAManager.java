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
package com.mbledug.blojsom.plugin.gatekeeper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.blojsom.blog.Blog;

import com.mbledug.blojsom.plugin.gatekeeper.provider.BlogQAProvider;
import com.mbledug.blojsom.plugin.gatekeeper.provider.BlojsomQAProvider;

/**
 * {@link QAManager} manages question answer retrieval from providers.
 * @author Cliffano
 */
public class QAManager {

    /**
     * An integer used for multiplication to the random decimal, this value
     * is then used to get the random object index on a given list.
     */
    private static final int RANDOM_MULTIPLIER = 10000;

    /**
     * A list of {@link BlojsomQAProvider}s.
     */
    private List mBlojsomQAProviders;

    /**
     * A list of {@link BlogQAProvider}s.
     */
    private List mBlogQAProviders;

    /**
     * Constructs {@link QAManager} with specified question answer providers.
     * @param blojsomQAProviders a list of {@link BlojsomQAProvider}s
     * @param blogQAProviders a list of {@link BlogQAProvider}s
     */
    public QAManager(
            final List blojsomQAProviders,
            final List blogQAProviders) {
        if (blojsomQAProviders == null) {
            throw new IllegalArgumentException(
                    "Blojsom QA Providers cannot be null.");
        }
        if (blogQAProviders == null) {
            throw new IllegalArgumentException(
                    "Blog QA Providers cannot be null.");
        }
        if (blojsomQAProviders.size() + blogQAProviders.size() == 0) {
            throw new IllegalArgumentException(
                    "There should at least be one provider.");
        }
        mBlojsomQAProviders = blojsomQAProviders;
        mBlogQAProviders = blogQAProviders;
    }

    /**
     * Gets a random question answer.
     * @param blog the blog requestion question answer
     * @return the random question answer
     */
    final QA getRandomQuestionAnswer(final Blog blog) {
        List questionAnswerList = getAllQuestionAnswer(blog);

        int randomPosition = Integer.parseInt(String.valueOf(
                (Math.round(Math.random() * RANDOM_MULTIPLIER))
                % questionAnswerList.size()));
        return (QA) questionAnswerList.get(randomPosition);
    }

    /**
     * Gets a list of question answer from all providers.
     * @param blog the blog requestion question answer
     * @return a list of all question answer
     */
    private List getAllQuestionAnswer(final Blog blog) {
        List questionAnswerList = new ArrayList();
        for (Iterator it = mBlojsomQAProviders.iterator(); it.hasNext();) {
            BlojsomQAProvider provider = (BlojsomQAProvider) it.next();
            questionAnswerList.addAll(provider.getQuestionAnswerList());
        }
        for (Iterator it = mBlogQAProviders.iterator(); it.hasNext();) {
            BlogQAProvider provider = (BlogQAProvider) it.next();
            questionAnswerList.addAll(provider.getQuestionAnswerList(blog));
        }
        return questionAnswerList;
    }
}
