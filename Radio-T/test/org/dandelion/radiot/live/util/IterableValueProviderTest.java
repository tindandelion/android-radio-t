package org.dandelion.radiot.live.util;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class IterableValueProviderTest {
    public static final List<String> values = asList("value1", "value2");
    public final ValueProvider<String>
            provider = new IterableValueProvider<String>(values);

    @Test
    public void create() throws Exception {
        assertThat(provider, not(nullValue()));
    }

    @Test
    public void whenCalledFirstTime_ReturnsFirstValue() throws Exception {
        assertThat(provider.value(), equalTo("value1"));
    }

    @Test
    public void whenCalledSecondTime_ReturnsSecondValue() throws Exception {
        provider.value();
        assertThat(provider.value(), equalTo("value2"));
    }

    @Test
    public void whenReachesLastElement_startsFromTheHead() throws Exception {
        provider.value();
        provider.value();
        assertThat(provider.value(), equalTo("value1"));
    }
}
