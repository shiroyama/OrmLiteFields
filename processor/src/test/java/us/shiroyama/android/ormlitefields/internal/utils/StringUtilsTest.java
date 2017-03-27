package us.shiroyama.android.ormlitefields.internal.utils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit Test for {@link StringUtils}
 *
 * @author Fumihiko Shiroyama
 */

public class StringUtilsTest {
    @Test
    public void camelToSnake_normal() throws Exception {
        String snakeCaseString = StringUtils.camelToSnake("fooBarBaz");
        assertThat(snakeCaseString, is("foo_bar_baz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void camelToSnake_error() throws Exception {
        StringUtils.camelToSnake(null);
    }

    @Test
    public void camelToSnake_allLowerCase() throws Exception {
        String actual = StringUtils.camelToSnake("foo");
        assertThat(actual, is("foo"));
    }

    @Test
    public void camelToSnake_allUpperCase() throws Exception {
        String actual = StringUtils.camelToSnake("BAR");
        // result always becomes lower cases
        assertThat(actual, is("bar"));
    }

    @Test
    public void camelToSnake_partiallyUpperCase() throws Exception {
        String actual = StringUtils.camelToSnake("fooID");
        // result always becomes lower cases
        assertThat(actual, is("foo_id"));

        actual = StringUtils.camelToSnake("barBAZMooMOO");
        assertThat(actual, is("bar_baz_moo_moo"));
    }

    @Test
    public void stripFirstAndLastUnderscore() throws Exception {
        String actual = StringUtils.stripFirstAndLastUnderscore("_foo");
        assertThat(actual, is("foo"));

        actual = StringUtils.stripFirstAndLastUnderscore("bar_");
        assertThat(actual, is("bar"));

        actual = StringUtils.stripFirstAndLastUnderscore("_baz_");
        assertThat(actual, is("baz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void stripFirstAndLastUnderscore_error() throws Exception {
        StringUtils.stripFirstAndLastUnderscore(null);
    }

}
