package com.mbledug.blojsom.plugin.iptocountry;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class IpToCountryDaoTest extends TestCase {

	static final Country EXPECTED_COUNTRY = new Country("GB", "GBR", "UNITED KINGDOM");

    public void testGetCountryReturnsTheExpectedCountry() {

        Map result = new HashMap();
        result.put(IpToCountryDao.COLUMN_COUNTRY_CODE2, EXPECTED_COUNTRY.getTwoCharCode());
        result.put(IpToCountryDao.COLUMN_COUNTRY_CODE3, EXPECTED_COUNTRY.getThreeCharCode());
        result.put(IpToCountryDao.COLUMN_COUNTRY_NAME, EXPECTED_COUNTRY.getName());

        JdbcTemplate jdbcTemplate = (JdbcTemplate) EasyMock.createStrictMock(JdbcTemplate.class);
        EasyMock.expect(jdbcTemplate.queryForMap("select * from iptocountry where ip_from < 234242432 and ip_to > 234242432")).andReturn(result);

        IpToCountryDao ipToCountryDao = new IpToCountryDao(jdbcTemplate);

        EasyMock.replay(new Object[]{jdbcTemplate});

        Country retrievedCountry = ipToCountryDao.getCountry(234242432l);
        assertEquals(EXPECTED_COUNTRY.getTwoCharCode(), retrievedCountry.getTwoCharCode());
        assertEquals(EXPECTED_COUNTRY.getThreeCharCode(), retrievedCountry.getThreeCharCode());
        assertEquals(EXPECTED_COUNTRY.getName(), retrievedCountry.getName());

        EasyMock.verify(new Object[]{jdbcTemplate});
    }

    public void testGetCountryDoesntFindAnyCountry() {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) EasyMock.createStrictMock(JdbcTemplate.class);
        EasyMock.expect(jdbcTemplate.queryForMap("select * from iptocountry where ip_from < 234242432 and ip_to > 234242432")).andThrow(new EmptyResultDataAccessException(0));

        IpToCountryDao ipToCountryDao = new IpToCountryDao(jdbcTemplate);

        EasyMock.replay(new Object[]{jdbcTemplate});

        try {
            ipToCountryDao.getCountry(234242432l);
            fail("EmptyResultDataAccessException should've been thrown.");
        } catch (EmptyResultDataAccessException erdae) {
            // expected
        }

        EasyMock.verify(new Object[]{jdbcTemplate});
    }
}
