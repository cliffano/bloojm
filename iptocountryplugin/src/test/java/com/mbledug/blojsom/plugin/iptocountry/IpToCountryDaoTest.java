package com.mbledug.blojsom.plugin.iptocountry;

import junit.framework.TestCase;

import org.springframework.dao.EmptyResultDataAccessException;

public class IpToCountryDaoTest extends TestCase {

    DataFixture mDataFixture;

    public void setUp() {
        mDataFixture = new DataFixture();
    }

    public void testGetCountryReturnsTheExpectedCountry() {
        Country expectedCountry = DataFixture.EXPECTED_COUNTRY;
        IpToCountryDao ipToCountryDao = new IpToCountryDao(mDataFixture.createMockJdbcTemplate(expectedCountry));
        Country retrievedCountry = ipToCountryDao.getCountry(234242432l);
        assertEquals(expectedCountry.getTwoCharCode(), retrievedCountry.getTwoCharCode());
        assertEquals(expectedCountry.getThreeCharCode(), retrievedCountry.getThreeCharCode());
        assertEquals(expectedCountry.getName(), retrievedCountry.getName());
    }

    public void testGetCountryDoesntFindAnyCountry() {
        IpToCountryDao ipToCountryDao = new IpToCountryDao(mDataFixture.createMockJdbcTemplate(new EmptyResultDataAccessException(0)));
        try {
            ipToCountryDao.getCountry(234242432l);
            fail("EmptyResultDataAccessException should've been thrown.");
        } catch (EmptyResultDataAccessException erdae) {
            // expected
        }
    }
}
