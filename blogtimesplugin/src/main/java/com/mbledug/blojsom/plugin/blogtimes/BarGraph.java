/**
 * Copyright (c) 2005-2007, Cliffano Subagio
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
package com.mbledug.blojsom.plugin.blogtimes;

/**
 * {@link BarGraph} provides details required to draw the bar graph image.
 * @author Cliffano Subagio
 */
final class BarGraph {

    /**
     * Time unit from java.util.Calendar class.
     */
    private int mCalendarUnit;

    /**
     * The value used to scale the time on the graph.
     */
    private int mScaler;

    /**
     * Interval value of the timeline.
     * E.g. if hour of day to be represented with interval of 2, then the
     * timeline will display 0, 2, 4, 6, ... 24
     */
    private int mInterval;

    /**
     * Maximum time unit value of a graph, used as the timeline end value.
     */
    private int mMaxValue;

    /**
     * Create an instance of {@link BarGraph}.
     * @param calendarUnit the calendar unit value. Use the time units available
     *          from java.util.Calendar class.
     * @param scaler time scaler value
     * @param interval timeline interval value
     * @param maxValue maximum time unit value
     */
    BarGraph(
            final int calendarUnit,
            final int scaler,
            final int interval,
            final int maxValue) {
        mCalendarUnit = calendarUnit;
        mScaler = scaler;
        mInterval = interval;
        mMaxValue = maxValue;
    }

    /**
     * Gets calendar unit value.
     * @return the calendar unit value
     */
    int getCalendarUnit() {
        return mCalendarUnit;
    }

    /**
     * Gets time scaler value.
     * @return the time scaler value
     */
    int getScaler() {
        return mScaler;
    }

    /**
     * Gets timeline interval value.
     * @return the timeline interval value
     */
    int getInterval() {
        return mInterval;
    }

    /**
     * Gets maximum time unit value.
     * @return the maximum time unit value
     */
    int getMaxValue() {
        return mMaxValue;
    }
}
