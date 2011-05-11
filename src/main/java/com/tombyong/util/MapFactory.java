package com.tombyong.util;

import java.beans.PropertyEditor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.PropertyEditorRegistrySupport;

/**
 * A builder style of constructing Map instance, can only do <code>put</code>
 * and <code>putAll</code>.
 * <p>
 * Default implementation of the Map instance is <code>LinkedHashMap</code>.
 * @author junyong
 * 
 * @param <K> key type of the map
 * @param <V> value type of the map
 */
public class MapFactory<K, V> extends PropertyEditorRegistrySupport {

	private Map<K, V> values;

	private Class<K> keyType;

	private Class<V> valueType;

	private MapFactory() {
		values = new LinkedHashMap<K, V>();
		registerDefaultEditors();
	}

	public Map<K, V> toMap() {
		return this.values;
	}

	/**
	 * @param mapStringValues string values in properties style
	 * @return Map instance represented in the <code>mapStringValues</code>
	 * parameter
	 * @throws IllegalArgumentException if can't convert the key or value to
	 * required type
	 */
	@SuppressWarnings("unchecked")
	public Map<K, V> toMap(String mapStringValues) {
		PropertyEditor editor = getDefaultEditor(Properties.class);
		editor.setAsText(mapStringValues);

		Properties prop = (Properties) editor.getValue();
		for (Map.Entry<?, ?> entry : prop.entrySet()) {
			K key = null;
			V value = null;

			if (keyType != String.class && valueType != Object.class) {
				PropertyEditor keyEditor = getDefaultEditor(keyType);
				if (keyEditor == null) {
					throw new IllegalArgumentException("can't find proper editor for type [" + keyType
							+ "], please register via the instance");
				}
				keyEditor.setAsText((String) entry.getKey());
				key = (K) keyEditor.getValue();
			}
			else {
				key = (K) entry.getKey();
			}

			if (valueType != String.class && valueType != Object.class) {
				PropertyEditor valueEditor = getDefaultEditor(valueType);
				if (valueEditor == null) {
					throw new IllegalArgumentException("can't find proper editor for type [" + valueType
							+ "], please register via the instance");
				}
				valueEditor.setAsText((String) entry.getValue());
				value = (V) valueEditor.getValue();
			}
			else {
				value = (V) entry.getValue();
			}

			values.put(key, value);
		}

		return values;
	}

	public MapFactory<K, V> put(K key, V value) {
		this.values.put(key, value);
		return this;
	}

	public MapFactory<K, V> putAll(Map<K, V> map) {
		this.values.putAll(map);
		return this;
	}

	public static <K, V> MapFactory<K, V> build(Class<K> keyType, Class<V> valueType) {
		MapFactory<K, V> mf = new MapFactory<K, V>();
		mf.keyType = keyType;
		mf.valueType = valueType;
		return mf;
	}

}
