package com.tombyong.beans;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

public abstract class BeanObjectGraphIntrospector {

	public static Map<String, Object> getBeanValuesAsMap(Object bean) {
		return getBeanValuesAsMap(bean, null);
	}

	public static Map<String, Object> getBeanValuesAsMap(Object bean, String[] ignoredProperties) {
		assert bean != null : "Please provide non null object";

		Map<String, Object> values = new HashMap<String, Object>();
		PropertyDescriptor[] descs = BeanUtils.getPropertyDescriptors(bean.getClass());
		for (PropertyDescriptor desc : descs) {
			if (ignoredProperties != null && Arrays.asList(ignoredProperties).contains(desc.getName())) {
				continue;
			}

			if (desc.getReadMethod() != null && desc.getWriteMethod() != null) {
				Object value = ReflectionUtils.invokeMethod(desc.getReadMethod(), bean);
				values.put(desc.getName(), value);
			}
		}

		return values;
	}

	public static <T> T parseMapValuesToObject(Map<String, Object> values, Class<T> requiredClass) {
		T bean = BeanUtils.instantiateClass(requiredClass);
		PropertyDescriptor[] descs = BeanUtils.getPropertyDescriptors(requiredClass);

		for (PropertyDescriptor desc : descs) {
			if (desc.getReadMethod() != null && desc.getWriteMethod() != null) {
				ReflectionUtils.invokeMethod(desc.getWriteMethod(), bean, values.get(desc.getName()));
			}
		}

		return bean;
	}
}
