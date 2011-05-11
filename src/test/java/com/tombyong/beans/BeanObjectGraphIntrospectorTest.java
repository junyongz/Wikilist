package com.tombyong.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.tombyong.wikilist.project.Project;

public class BeanObjectGraphIntrospectorTest {
	@Test
	public void testTransformProjectBeanToMapValues() {
		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");

		Map<String, Object> values = BeanObjectGraphIntrospector.getBeanValuesAsMap(project);
		assertEquals("tester", values.get("name"));
		assertEquals("junyong", values.get("createdBy"));
		assertNull(values.get("createdDate"));
	}

	@Test
	public void testTransformMapValuesToProjectBean() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("name", "hello world");
		values.put("createdBy", "peishan");

		Project project = BeanObjectGraphIntrospector.parseMapValuesToObject(values, Project.class);

		assertEquals("hello world", project.getName());
		assertEquals("peishan", project.getCreatedBy());
		assertNull(project.getCreatedDate());
	}
}
