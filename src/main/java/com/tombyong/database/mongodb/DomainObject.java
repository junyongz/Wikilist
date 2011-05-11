package com.tombyong.database.mongodb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.tombyong.beans.BeanObjectGraphIntrospector;

/**
 * A wrapper on the actual domain object to do the low level mongo db job, such
 * as save, remove. Also, this wrapper can be used to retrieve objects from
 * mongo db, via <code>list</code> or <code>one</code> methods.
 * <p>
 * This wrapper consists of various static method which could help to construct
 * a list of wrapper or just a wrapper. After which there are multiple chaining
 * methods which easily solve the complex task such as assign id, setting
 * ignored properties for copy over from object graph to mongo db.
 * <p>
 * This wrapper will store the last error after run a mongo db command, via
 * {@link #lastError()}, use this for further analysis.
 * @author junyong
 * 
 * @param <T> actual domain object type
 */
public class DomainObject<T> {
	private String id;

	private DB mongoDb;

	private CommandResult saveError;

	private T domainObject;

	private String collectionName;

	private String[] ignoredProperties;

	private boolean ignoredNotFoundIdProperty = false;

	private DomainObject() {
	}

	public static <T> DomainObject<T> forDomain(T domainObject, String collectionName, String[] ignoredProperties,
			DB mongoDb) {
		DomainObject<T> o = new DomainObject<T>();
		o.domainObject = domainObject;
		o.mongoDb = mongoDb;
		o.collectionName = collectionName;
		o.ignoredProperties = ignoredProperties;
		return o;
	}

	public static <T> DomainObject<T> forDomain(T domainObject, String collectionName, DB mongoDb) {
		DomainObject<T> o = new DomainObject<T>();
		o.domainObject = domainObject;
		o.mongoDb = mongoDb;
		o.collectionName = collectionName;
		return o;
	}

	public static <T> DomainObject<T> forDomain(T domainObject, DB mongoDb) {
		DomainObject<T> o = new DomainObject<T>();
		o.domainObject = domainObject;
		o.mongoDb = mongoDb;
		o.collectionName = ClassUtils.getShortName(domainObject.getClass()).toLowerCase();
		return o;
	}

	public static <T> DomainObject<T> forDomain(T domainObject, DB mongoDb, String dbName) {
		DomainObject<T> o = new DomainObject<T>();
		o.domainObject = domainObject;
		o.mongoDb = mongoDb;
		o.collectionName = ClassUtils.getShortName(domainObject.getClass()).toLowerCase();
		return o;
	}

	public static <T> List<DomainObject<T>> forDomains(Collection<T> domainObjects, String collectionName,
			String[] ignoredProperties, DB mongoDb) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		for (T domainObject : domainObjects) {
			DomainObject<T> o = new DomainObject<T>();
			o.domainObject = domainObject;
			o.mongoDb = mongoDb;
			o.ignoredProperties = ignoredProperties;
			o.collectionName = collectionName;
			domains.add(o);
		}
		return domains;
	}

	public static <T> List<DomainObject<T>> forDomains(Collection<T> domainObjects, String collectionName, DB mongoDb) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		for (T domainObject : domainObjects) {
			DomainObject<T> o = new DomainObject<T>();
			o.domainObject = domainObject;
			o.mongoDb = mongoDb;
			o.collectionName = collectionName;
			domains.add(o);
		}
		return domains;
	}

	public static <T> List<DomainObject<T>> forDomains(Collection<T> domainObjects, DB mongoDb) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		for (T domainObject : domainObjects) {
			DomainObject<T> o = new DomainObject<T>();
			o.domainObject = domainObject;
			o.mongoDb = mongoDb;
			o.collectionName = ClassUtils.getShortName(domainObject.getClass()).toLowerCase();
			domains.add(o);
		}
		return domains;
	}

	public static <T> List<DomainObject<T>> forDomains(Collection<T> domainObjects, DB mongoDb, String dbName) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		for (T domainObject : domainObjects) {
			DomainObject<T> o = new DomainObject<T>();
			o.domainObject = domainObject;
			o.mongoDb = mongoDb;
			o.collectionName = ClassUtils.getShortName(domainObject.getClass()).toLowerCase();
			domains.add(o);
		}
		return domains;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<DomainObject<T>> list(String collectionName, Class<T> requiredClass, DB mongoDb) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		DBCursor cursor = mongoDb.getCollection(collectionName).find();
		for (DBObject object : cursor) {
			T obj = (T) BeanObjectGraphIntrospector.parseMapValuesToObject(object.toMap(), requiredClass);
			ObjectId oid = (ObjectId) object.get("_id");
			DomainObject<T> o = forDomain(obj, collectionName, mongoDb).id(oid.toStringMongod());
			domains.add(o);
		}
		return domains;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<DomainObject<T>> list(String collectionName, Class<T> requiredClass,
			Map<String, ?> conditions, DB mongoDb) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		DBCursor cursor = mongoDb.getCollection(collectionName).find(new BasicDBObject(conditions));
		for (DBObject object : cursor) {
			T obj = (T) BeanObjectGraphIntrospector.parseMapValuesToObject(object.toMap(), requiredClass);
			ObjectId oid = (ObjectId) object.get("_id");
			DomainObject<T> o = forDomain(obj, collectionName, mongoDb).id(oid.toStringMongod());
			domains.add(o);
		}
		return domains;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<DomainObject<T>> list(String collectionName, Class<T> requiredClass, DBObject conditions,
			DB mongoDb) {
		List<DomainObject<T>> domains = new ArrayList<DomainObject<T>>();
		DBCursor cursor = mongoDb.getCollection(collectionName).find(conditions);
		for (DBObject object : cursor) {
			T obj = (T) BeanObjectGraphIntrospector.parseMapValuesToObject(object.toMap(), requiredClass);
			ObjectId oid = (ObjectId) object.get("_id");
			DomainObject<T> o = forDomain(obj, collectionName, mongoDb).id(oid.toStringMongod());
			domains.add(o);
		}
		return domains;
	}

	@SuppressWarnings("unchecked")
	public static <T> DomainObject<T> one(String collectionName, Class<T> requiredClass, Map<String, ?> conditions,
			DB mongoDb) {
		DBObject object = mongoDb.getCollection(collectionName).findOne(new BasicDBObject(conditions));
		if (object == null) {
			return new DomainObject<T>();
		}
		T obj = (T) BeanObjectGraphIntrospector.parseMapValuesToObject(object.toMap(), requiredClass);
		ObjectId oid = (ObjectId) object.get("_id");
		DomainObject<T> o = forDomain(obj, collectionName, mongoDb).id(oid.toStringMongod());
		return o;
	}

	@SuppressWarnings("unchecked")
	public static <T> DomainObject<T> one(String collectionName, Class<T> requiredClass, DBObject conditions, DB mongoDb) {
		DBObject object = mongoDb.getCollection(collectionName).findOne(conditions);
		if (object == null) {
			return new DomainObject<T>();
		}
		T obj = (T) BeanObjectGraphIntrospector.parseMapValuesToObject(object.toMap(), requiredClass);
		ObjectId oid = (ObjectId) object.get("_id");
		DomainObject<T> o = forDomain(obj, collectionName, mongoDb).id(oid.toStringMongod());
		return o;
	}

	public DomainObject<T> ignore(String[] ignoredProperties) {
		this.ignoredProperties = ignoredProperties;
		return this;
	}

	public DomainObject<T> ignoreNotFoundIdProperty() {
		this.ignoredNotFoundIdProperty = true;
		return this;
	}

	public DomainObject<T> save() {
		BasicDBObject basicDbObject = new BasicDBObject(BeanObjectGraphIntrospector.getBeanValuesAsMap(domainObject,
				ignoredProperties));
		if (this.id != null) {
			basicDbObject.put("_id", new ObjectId(this.id));
		}

		WriteResult wr = this.mongoDb.getCollection(collectionName).save(basicDbObject);
		this.saveError = wr.getLastError();
		if (this.id == null) {
			this.id = basicDbObject.getString("_id");
		}

		return this;
	}

	public DomainObject<T> id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Fill id value from this instance to the actual domain object, this should
	 * be done immediately after {@link #save()} or
	 * {@link #one(String, Class, Map, DB)} , in order for application do work
	 * properly, example for association.
	 * @param idPropertyName id property name of the actual domain object;
	 * @return this instance (for chaining purpose)
	 * @throws IllegalArgumentException if the id property of the actual domain
	 * object is not in java.lang.String type.
	 */
	public DomainObject<T> fillId(String idPropertyName) {
		Method idMethod = BeanUtils.getPropertyDescriptor(domainObject.getClass(), idPropertyName).getWriteMethod();
		if (idMethod == null) {
			if (!this.ignoredNotFoundIdProperty) {
				throw new IllegalArgumentException("Sorry, domain object [" + domainObject.getClass()
						+ "] doesn't have the id property [" + idPropertyName + "]");
			}
			return this;
		}
		Class<?> idClassType = idMethod.getParameterTypes()[0];
		if (idClassType != String.class) {
			throw new IllegalArgumentException("Sorry, mongo id must be java.lang.String type, but not [" + idClassType
					+ "]");
		}

		ReflectionUtils.invokeMethod(idMethod, domainObject, id);
		return this;
	}

	/**
	 * Transfer id value from the actual domain object to this instance, mostly
	 * for {@link #remove()} purpose.
	 * @param idPropertyName id property name of the actual domain object;
	 * @return this instance (for chaining purpose)
	 * @throws IllegalArgumentException if the id property of the actual domain
	 * object is not in java.lang.String type.
	 */
	public DomainObject<T> transferId(String idPropertyName) {
		Method idMethod = BeanUtils.getPropertyDescriptor(domainObject.getClass(), idPropertyName).getReadMethod();
		if (idMethod == null) {
			if (!this.ignoredNotFoundIdProperty) {
				throw new IllegalArgumentException("Sorry, domain object [" + domainObject.getClass()
						+ "] doesn't have the id property [" + idPropertyName + "]");
			}
			return this;
		}
		Class<?> idClassType = idMethod.getReturnType();
		if (idClassType != String.class) {
			throw new IllegalArgumentException("Sorry, mongo id must be java.lang.String type, but not [" + idClassType
					+ "]");
		}
		this.id = (String) ReflectionUtils.invokeMethod(idMethod, domainObject);
		if (!StringUtils.hasLength(this.id)) {
			this.id = null;
		}
		return this;
	}

	public T get() {
		return this.domainObject;
	}

	public String id() {
		return this.id;
	}

	public CommandResult lastError() {
		return this.saveError;
	}

	public void remove() {
		if (this.id == null) {
			throw new IllegalArgumentException(
					"Please don't remove a non-persisted record, or you can do a transfer of id first");
		}

		WriteResult wr = mongoDb.getCollection(collectionName).remove(new BasicDBObject("_id", new ObjectId(this.id)));
		this.saveError = wr.getLastError();
	}
}
