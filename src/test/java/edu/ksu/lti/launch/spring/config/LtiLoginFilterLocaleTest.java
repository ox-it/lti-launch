package edu.ksu.lti.launch.spring.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Locale;

import static org.junit.Assert.*;

public class LtiLoginFilterLocaleTest {

    private LtiLoginFilter filter;

    @Before
    public void setUp() {
        filter = new LtiLoginFilter(new AntPathRequestMatcher("/launch"));
    }

    @Test
    public void testToLocaleGood() {
        {
            Locale fr = filter.toLocale("fr");
            assertEquals(Locale.FRENCH, fr);
        }
        {
            Locale en = filter.toLocale("en");
            assertEquals(Locale.ENGLISH, en);
        }
    }

    @Test
    public void testLocaleWithCountryDash() {
        {
            Locale uk = filter.toLocale("en-GB");
            assertEquals(Locale.UK, uk);
        }
        {
            Locale fr = filter.toLocale("fr-FR");
            assertEquals(Locale.FRANCE, fr);
        }
    }

    @Test
    public void testLocaleWithCountryUnderscore() {
        {
            Locale uk = filter.toLocale("en_GB");
            assertEquals(Locale.UK, uk);
        }
        {
            Locale fr = filter.toLocale("fr_FR");
            assertEquals(Locale.FRANCE, fr);
        }
    }

    @Test
    public void testBadLocale() {
        Locale def = filter.toLocale("!!!not a valid locale");
        assertNull(def);
    }

    @Test
    public void testUnknownLocale() {
        Locale xx = filter.toLocale("xx_XX");
        assertNotNull(xx);
        assertEquals("xx", xx.getLanguage());
        assertEquals("XX", xx.getCountry());
    }

    @Test
    public void testEmptyLocale() {
        Locale empty = filter.toLocale("");
        assertNull(empty);
    }

    @Test
    public void testNullLocale() {
        Locale empty = filter.toLocale(null);
        assertNull(empty);
    }
}
