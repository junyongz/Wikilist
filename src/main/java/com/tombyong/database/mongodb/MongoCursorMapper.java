package com.tombyong.database.mongodb;

import com.mongodb.DBObject;

public interface MongoCursorMapper<T> {
	public T mapCursor(DBObject dbObject);
}
