/**
 * Copyright (c) 2005-2006, Cliffano Subagio
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
 *   * Neither the name of Mbledug nor the names of its contributors
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link BarGraphFactory} provides a factory method to determine which
 * {@link BarGraph} to be used based on flavor.
 * @author Cliffano
 */
final class BarGraphFactory {

    /**
     * Flavor String for second-of-minute.
     */
    private static final String FLAVOR_SECOND_OF_MINUTE = "second-of-minute";

    /**
     * Flavor String for minute-of-hour.
     */
    private static final String FLAVOR_MINUTE_OF_HOUR = "minute-of-hour";

    /**
     * Flavor String for hour-of-day.
     */
    private static final String FLAVOR_HOUR_OF_DAY = "hour-of-day";

    /**
     * Default flavor String.
     */
    private static final String FLAVOR_DEFAULT = FLAVOR_HOUR_OF_DAY;

    /**
     * {@link BarGraph} for second-of-minute flavor.
     */
    private static final BarGraph BAR_GRAPH_SECOND_OF_MINUTE = new BarGraph(
            Calendar.SECOND,
            TimeUnit.SECONDS_IN_MINUTE,
            5,
            TimeUnit.MAX_SECOND_IN_MINUTE);

    /**
     * {@link BarGraph} for minute-of-hour flavor.
     */
    private static final BarGraph BAR_GRAPH_MINUTE_OF_HOUR = new BarGraph(
            Calendar.MINUTE,
            TimeUnit.SECONDS_IN_HOUR,
            5,
            TimeUnit.MAX_MINUTE_IN_HOUR);

    /**
     * {@link BarGraph} for hour-of-day flavor.
     */
    private static final BarGraph BAR_GRAPH_HOUR_OF_DAY = new BarGraph(
            Calendar.HOUR_OF_DAY,
            TimeUnit.SECONDS_IN_DAY,
            2,
            TimeUnit.MAX_HOUR_IN_DAY);

    /**
     * Map of {@link BarGraph}s, containing flavor-{@link BarGraph}, as
     * key-value pairs.
     */
    private static Map mBarGraphs;

    static {
        mBarGraphs = new HashMap();
        mBarGraphs.put(FLAVOR_SECOND_OF_MINUTE, BAR_GRAPH_SECOND_OF_MINUTE);
        mBarGraphs.put(FLAVOR_MINUTE_OF_HOUR, BAR_GRAPH_MINUTE_OF_HOUR);
        mBarGraphs.put(FLAVOR_HOUR_OF_DAY, BAR_GRAPH_HOUR_OF_DAY);
    }

    /**
     * Hides default constructor.
     */
    private BarGraphFactory() {
    }

    /**
     * Gets the {@link BarGraph} based on the specified flavor. Default
     * {@link BarGraph} will be returned if flavor is null.
     * If flavor is unknown, then IllegalArgumentException will be thrown at
     * run time.
     * @param flavor the flavor
     * @return the {@link BarGraph} based on the flavor
     */
    static final BarGraph getBarGraph(final String flavor) {
        BarGraph barGraph;
        if (flavor == null) {
            barGraph = (BarGraph) mBarGraphs.get(FLAVOR_DEFAULT);
        } else {
            barGraph = (BarGraph) mBarGraphs.get(flavor);
            if (barGraph == null) {
                throw new IllegalArgumentException("Flavor: " + flavor
                        + " is invalid.");
            }
        }
        return barGraph;
    }
}
