package com.tombyong.database.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoException;

public interface MongoDbAction<T> {
	public T doInMongoDb(DB db) throws MongoException;
}
