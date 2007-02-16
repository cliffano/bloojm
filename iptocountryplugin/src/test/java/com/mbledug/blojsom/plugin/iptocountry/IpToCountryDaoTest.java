package com.mbledug.blojsom.plugin.iptocountry;

import org.springframework.dao.EmptyResultDataAccessException;

import junit.framework.TestCase;

public class IpToCountryDaoTest extends TestCase {

    DataFixture mDataFixture;

    public void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testGetCountryCodeReturnsCountryCode() {
        String countryCode = "AU";
        IpToCountryDao ipToCountryDao = new IpToCountryDao(mDataFixture.createMockJdbcTemplate(countryCode));
        assertEquals(countryCode, ipToCountryDao.getCountryCode(234242432l));
    }

    public void testGetCountryCodeDoesntFindCountryCode() {
        IpToCountryDao ipToCountryDao = new IpToCountryDao(mDataFixture.createMockJdbcTemplate(new EmptyResultDataAccessException(0)));
        try {
            ipToCountryDao.getCountryCode(234242432l);
            fail("EmptyResultDataAccessException should've been thrown.");
        } catch (EmptyResultDataAccessException erdae) {
            // expected
        }
    }
}
