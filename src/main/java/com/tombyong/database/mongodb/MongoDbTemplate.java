package com.tombyong.database.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

public class MongoDbTemplate {
	private static final Logger logger = LoggerFactory.getLogger(MongoDbTemplate.class);

	private Mongo mongo;

	private String dbName;

	public MongoDbTemplate(Mongo mongo, String dbName) {
		this.mongo = mongo;
		this.dbName = dbName;
	}

	public <T> T execute(MongoDbAction<T> action) {
		DB db = this.mongo.getDB(this.dbName);

		try {
			db.requestStart();

			T result = action.doInMongoDb(db);

			return result;
		}
		catch (MongoException ex) {
			logger.error("failed to execute, msg=" + db.getLastError().getErrorMessage() + ", ex=" + ex);
		}
		finally {
			db.requestDone();
		}

		return null;
	}

	public void removeByPrimaryKey(final String collectionName, final String mongoId) {
		execute(new MongoDbAction<Object>() {
			@Override
			public Object doInMongoDb(DB db) throws MongoException {
				WriteResult wr = db.getCollection(collectionName).remove(
						new BasicDBObject("_id", new ObjectId(mongoId)));
				reportError(collectionName, "REMOVE_BY_PRIMARY_KEY", wr.getLastError());
				return null;
			}
		});
	}

	public <T> T retrieveByPrimaryKey(final String collectionName, final String mongoId, final Class<T> requiredClass) {
		return execute(new MongoDbAction<T>() {
			@Override
			public T doInMongoDb(DB db) throws MongoException {
				DomainObject<T> o = DomainObject.one(collectionName, requiredClass, (DBObject) new BasicDBObject("_id",
						new ObjectId(mongoId)), db);
				if (o.get() == null) {
					o = null;
					return null;
				}
				return o.fillId("id").get();
			}
		});
	}

	public String saveOrUpdateRecord(final String collectionName, final Object record, final String[] ignoredProperties) {
		return execute(new MongoDbAction<String>() {

			@Override
			public String doInMongoDb(DB db) throws MongoException {
				DomainObject<Object> o = DomainObject.forDomain(record, collectionName, ignoredProperties, db);
				o.transferId("id").save();
				reportError(collectionName, "SAVE_UPDATE_RECORD", o.lastError());

				return o.id();
			}
		});
	}

	public <T> List<T> findRecordsByJsonString(final String collectionName, final String json,
			final MongoCursorMapper<T> cursorMapper) {
		return execute(new MongoDbAction<List<T>>() {
			@Override
			public List<T> doInMongoDb(DB db) throws MongoException {
				DBCursor cursor = db.getCollection(collectionName).find((DBObject) com.mongodb.util.JSON.parse(json));
				List<T> records = new ArrayList<T>();

				for (DBObject obj : cursor) {
					records.add(cursorMapper.mapCursor(obj));
				}
				return records;
			}
		});
	}

	public <T> List<T> findRecordsByMapValues(final String collectionName, final Map<?, ?> values,
			final MongoCursorMapper<T> cursorMapper) {
		return execute(new MongoDbAction<List<T>>() {
			@Override
			public List<T> doInMongoDb(DB db) throws MongoException {
				DBCursor cursor = db.getCollection(collectionName).find(new BasicDBObject(values));
				List<T> records = new ArrayList<T>();

				for (DBObject obj : cursor) {
					records.add(cursorMapper.mapCursor(obj));
				}
				return records;
			}
		});
	}

	protected void reportError(String collectionName, String action, CommandResult cr) {
		if (!cr.ok()) {
			logger.error("failed to [{}] for collection {}, error {}, exception detail {}", new Object[] { action,
					collectionName, cr.getErrorMessage(), cr.getException() });
		}
	}
}
