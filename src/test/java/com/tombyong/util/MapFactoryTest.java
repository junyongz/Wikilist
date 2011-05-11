package com.tombyong.util;

import static org.junit.Assert.assertEquals;

import java.util.Currency;
import java.util.Map;

import org.junit.Test;

public class MapFactoryTest {
	@Test
	public void putStringValues() {
		Map<String, String> values = MapFactory.build(String.class, String.class).put("what", "what")
				.put("hello", "hello").put("world", "world").toMap();
		assertEquals("what", values.get("what"));
		assertEquals("hello", values.get("hello"));
		assertEquals("world", values.get("world"));
	}

	@Test
	public void putNumberValues() {
		Map<String, ? extends Number> values = MapFactory.build(String.class, Number.class).put("what", 1l)
				.put("hello", 3f).put("world", 4).toMap();
		assertEquals(1l, values.get("what"));
		assertEquals(3f, values.get("hello"));
		assertEquals(4, values.get("world"));
	}

	@Test
	public void putPropertiesStyleValues() {
		Map<String, Long> values = MapFactory.build(String.class, Long.class).toMap("what=1\nhello=3\nworld=4");
		assertEquals(Long.valueOf(1), values.get("what"));
		assertEquals(Long.valueOf(3), values.get("hello"));
		assertEquals(Long.valueOf(4), values.get("world"));
	}

	@Test
	public void putPropertiesStyleValuesOtherType() {
		Map<String, Currency> values = MapFactory.build(String.class, Currency.class).toMap(
				"what=SGD\nhello=MYR\nworld=USD");
		assertEquals("SGD", values.get("what").getCurrencyCode());
		assertEquals("MYR", values.get("hello").getCurrencyCode());
		assertEquals("USD", values.get("world").getCurrencyCode());
	}

	@Test
	public void putPropertiesStyleWithPutAndPutAllCombinationValues() {
		Map<String, Long> values = MapFactory.build(String.class, Long.class).put("123", 123l)
				.toMap("what=1\nhello=3\nworld=4");
		assertEquals(Long.valueOf(1), values.get("what"));
		assertEquals(Long.valueOf(3), values.get("hello"));
		assertEquals(Long.valueOf(4), values.get("world"));
		assertEquals(Long.valueOf(123), values.get("123"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void putPropertiesStyleWithUnknownTypeEditor() {
		MapFactory.build(String.class, MapFactory.class).toMap("what=1\nhello=3\nworld=4");
	}

	@Test
	public void putPropertiesStyleWithObjectType() {
		Map<String, Object> values = MapFactory.build(String.class, Object.class).toMap("what=1\nhello=3\nworld=4");
		assertEquals("1", values.get("what"));
		assertEquals("3", values.get("hello"));
		assertEquals("4", values.get("world"));
	}
}
