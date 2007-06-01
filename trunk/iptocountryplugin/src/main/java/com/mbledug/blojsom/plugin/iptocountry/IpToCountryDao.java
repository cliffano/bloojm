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
package com.mbledug.blojsom.plugin.iptocountry;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * DAO for retrieving IpToCountry data.
 * @author Cliffano
 */
class IpToCountryDao {

    /**
     * Column name for two-character country code.
     */
    static final String COLUMN_COUNTRY_CODE2 = "country_code2";

    /**
     * Column name for three-character country code.
     */
    static final String COLUMN_COUNTRY_CODE3 = "country_code3";

    /**
     * Column name for country name.
     */
    static final String COLUMN_COUNTRY_NAME = "country_name";

    /**
     * JdbcTemplate used to execute the SQL query.
     */
    private JdbcTemplate mJdbcTemplate;

    /**
     * Constructs {@link IpToCountryDao}.
     */
    IpToCountryDao() {
        mJdbcTemplate = null;
    }

    /**
     * Constructs {@link IpToCountryDao} with specified JdbcTemplate.
     * @param jdbcTemplate the JdbcTemplate
     */
    IpToCountryDao(final JdbcTemplate jdbcTemplate) {
        mJdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets the country code based on the specified IP number.
     * @param ipNumber the IP Number used to retrieve the country from
     * @return the country code
     */
    public Country getCountry(final long ipNumber) {

        String sql = "select * from iptocountry where ip_from < "
                + ipNumber + " and ip_to > " + ipNumber;

        Map result = mJdbcTemplate.queryForMap(sql);

        return new Country(
                String.valueOf(result.get(COLUMN_COUNTRY_CODE2)),
                String.valueOf(result.get(COLUMN_COUNTRY_CODE3)),
                String.valueOf(result.get(COLUMN_COUNTRY_NAME)));
    }
}
