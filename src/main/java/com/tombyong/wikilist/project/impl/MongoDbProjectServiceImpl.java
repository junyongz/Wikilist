package com.tombyong.wikilist.project.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.tombyong.beans.BeanObjectGraphIntrospector;
import com.tombyong.database.mongodb.DomainObject;
import com.tombyong.database.mongodb.MongoCursorMapper;
import com.tombyong.database.mongodb.MongoDbAction;
import com.tombyong.database.mongodb.MongoDbTemplate;
import com.tombyong.wikilist.project.Item;
import com.tombyong.wikilist.project.Project;
import com.tombyong.wikilist.project.ProjectService;

public class MongoDbProjectServiceImpl implements ProjectService {

	private String projectDbCollectionName = "projects";

	private String itemDbCollectionName = "items";

	private MongoDbTemplate mongoDbTemplate;

	public MongoDbProjectServiceImpl(Mongo mongo, String dbName) {
		this.mongoDbTemplate = new MongoDbTemplate(mongo, dbName);
	}

	public MongoDbProjectServiceImpl(MongoDbTemplate mongoDbTemplate) {
		this.mongoDbTemplate = mongoDbTemplate;
	}

	public void setProjectDbCollectionName(String projectDbCollectionName) {
		this.projectDbCollectionName = projectDbCollectionName;
	}

	public void setItemDbCollectionName(String itemDbCollectionName) {
		this.itemDbCollectionName = itemDbCollectionName;
	}

	@Override
	public Project getProjectByPrimaryKey(String id) {
		Project project = mongoDbTemplate.retrieveByPrimaryKey(this.projectDbCollectionName, id, Project.class);
		if (project != null) {
			List<Item> items = mongoDbTemplate.findRecordsByJsonString(this.itemDbCollectionName,
					"{\"belongToProjectId\":\"" + id + "\"}", new MongoCursorMapper<Item>() {

						@SuppressWarnings("unchecked")
						@Override
						public Item mapCursor(DBObject dbObject) {
							Item item = BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(), Item.class);
							item.setId(((ObjectId) dbObject.get("_id")).toStringMongod());
							return item;
						}
					});
			if (items != null && !items.isEmpty()) {
				project.setItems(new HashSet<Item>(items));
			}
		}

		return project;
	}

	@Override
	public Collection<Project> listAllProjects(final String username) {
		return mongoDbTemplate.findRecordsByJsonString(this.projectDbCollectionName, "{\"createdBy\":\"" + username
				+ "\"}", new MongoCursorMapper<Project>() {

			@SuppressWarnings("unchecked")
			@Override
			public Project mapCursor(DBObject dbObject) {
				Project proj = BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(), Project.class);
				proj.setId(((ObjectId) dbObject.get("_id")).toStringMongod());
				return proj;
			}
		});
	}

	@Override
	public void removeProject(final Project project) {
		mongoDbTemplate.execute(new MongoDbAction<Object>() {

			@Override
			public Object doInMongoDb(DB db) throws MongoException {
				db.getCollection(itemDbCollectionName).remove(new BasicDBObject("belongToProjectId", project.getId()));
				DomainObject.forDomain(project, projectDbCollectionName, db).transferId("id").remove();
				return null;
			}
		});
	}

	@Override
	public Project saveProject(final Project project) {
		return mongoDbTemplate.execute(new MongoDbAction<Project>() {
			@Override
			public Project doInMongoDb(DB db) throws MongoException {
				DomainObject<Project> proj = DomainObject.forDomain(project, projectDbCollectionName,
						new String[] { "items" }, db).transferId("id");
				proj.save().fillId("id");

				if (project.getItems() != null) {
					List<DomainObject<Item>> items = DomainObject.forDomains(project.getItems(), itemDbCollectionName,
							db);
					for (DomainObject<Item> item : items) {
						item.get().setBelongToProjectId(project.getId());
						item.save().fillId("id");
					}
				}
				return project;
			}
		});
	}

	@Override
	public Project addOrUpdateItem(Project project, Item item) {
		item.setBelongToProjectId(project.getId());
		item.setId(mongoDbTemplate.saveOrUpdateRecord(this.itemDbCollectionName, item, null));
		
		boolean foundExisting = false;
		for (Item existing : project.getItems()) {
			if (existing.getId().equals(item.getId())) {
				project.removeItem(existing);
				project.addItem(item);
				foundExisting = true;
				break;
			}
		}
		if (!foundExisting) {
			project.addItem(item);
		}

		return project;
	}

	@Override
	public Project removeItem(Project project, Item item) {
		if (project.getId().equals(item.getBelongToProjectId())) {
			mongoDbTemplate.removeByPrimaryKey(this.itemDbCollectionName, item.getId());
			project.removeItem(item);
		}
		return project;
	}
}
