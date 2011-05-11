package com.tombyong.beans;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpringExpressionTest {
	@SuppressWarnings("unchecked")
	@Test
	public void parseValuesToMapObject() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("{\"b\":\"c\", \"d\":\"e\"}");
		Map<String, String> values = exp.getValue(Map.class);
		Assert.assertEquals("c", values.get("b"));
		Assert.assertEquals("e", values.get("d"));
	}

	@Test
	public void parseJsonStringToMapObject() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Long> values = mapper.readValue("{\"b\":345, \"d\":678}",
				TypeFactory.mapType(HashMap.class, String.class, Long.class));
		Assert.assertEquals(Long.valueOf(345), values.get("b"));
		Assert.assertEquals(Long.valueOf(678), values.get("d"));
	}
}
