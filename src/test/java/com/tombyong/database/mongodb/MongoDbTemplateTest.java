package com.tombyong.database.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.tombyong.beans.BeanObjectGraphIntrospector;
import com.tombyong.util.MapFactory;
import com.tombyong.wikilist.project.Project;

public class MongoDbTemplateTest {

	private Mongo mongoDb;

	private MongoDbTemplate mongoDbTemplate;

	@Before
	public void setUp() throws UnknownHostException, MongoException {
		mongoDb = new Mongo("127.0.0.1");
		mongoDbTemplate = new MongoDbTemplate(mongoDb, "test");
	}

	@After
	public void tearDown() {
		mongoDb.getDB("test").getCollection("project").drop();
		mongoDb.getDB("test").getCollection("item").drop();
	}

	@Test
	public void testSaveObjectAndRetrieve() {
		Project project = new Project();
		project.setCreatedBy("junyong");
		project.setName("moving house");

		String id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		project = mongoDbTemplate.retrieveByPrimaryKey("project", id, Project.class);
		assertNotNull(project.getId());
		assertEquals("moving house", project.getName());
		assertEquals("junyong", project.getCreatedBy());
	}

	@Test
	public void testSaveObjectAndRemove() {
		Project project = new Project();
		project.setCreatedBy("junyong");
		project.setName("moving house");

		String id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		mongoDbTemplate.removeByPrimaryKey("project", id);
		assertNull(mongoDbTemplate.retrieveByPrimaryKey("project", id, Project.class));
	}

	@Test
	public void testSaveAndRetrieveByJsonString() {
		Project project = new Project();
		project.setCreatedBy("junyong");
		project.setName("moving house");

		String id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		List<Project> records = (List<Project>) mongoDbTemplate.findRecordsByJsonString("project",
				"{\"createdBy\":\"junyong\"}", new MongoCursorMapper<Project>() {

					@SuppressWarnings("unchecked")
					@Override
					public Project mapCursor(DBObject dbObject) {
						return BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(), Project.class);
					}
				});
		assertEquals(1, records.size());
		assertEquals("junyong", records.iterator().next().getCreatedBy());
		assertEquals("moving house", records.iterator().next().getName());
	}

	@Test
	public void testSaveAndRetrieveMoreProjectsByJsonString() {
		Project project = new Project();
		project.setCreatedBy("junyong");
		project.setName("moving house");

		String id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		Project project2 = new Project();
		project2.setCreatedBy("junyong");
		project2.setName("buy a big piano");

		id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		List<Project> records = (List<Project>) mongoDbTemplate.findRecordsByJsonString("project",
				"{\"createdBy\":\"junyong\"}", new MongoCursorMapper<Project>() {

					@SuppressWarnings("unchecked")
					@Override
					public Project mapCursor(DBObject dbObject) {
						Project proj = BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(),
								Project.class);
						proj.setId(((ObjectId) dbObject.get("_id")).toStringMongod());
						return proj;
					}
				});
		assertEquals(2, records.size());

		records = (List<Project>) mongoDbTemplate.findRecordsByJsonString("project", "{\"createdBy\":\"peishan\"}",
				new MongoCursorMapper<Project>() {

					@SuppressWarnings("unchecked")
					@Override
					public Project mapCursor(DBObject dbObject) {
						Project proj = BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(),
								Project.class);
						proj.setId(((ObjectId) dbObject.get("_id")).toStringMongod());
						return proj;
					}
				});
		assertEquals(0, records.size());
	}

	@Test
	public void testSaveAndRetrieveMoreProjectsUsingMapFactory() {
		Project project = new Project();
		project.setCreatedBy("junyong");
		project.setName("moving house");

		String id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		Project project2 = new Project();
		project2.setCreatedBy("junyong");
		project2.setName("buy a big piano");

		id = mongoDbTemplate.saveOrUpdateRecord("project", project, new String[] { "items" });
		assertNotNull(id);

		List<Project> records = (List<Project>) mongoDbTemplate.findRecordsByMapValues("project",
				MapFactory.build(String.class, Object.class).put("createdBy", "junyong").toMap(),
				new MongoCursorMapper<Project>() {

					@SuppressWarnings("unchecked")
					@Override
					public Project mapCursor(DBObject dbObject) {
						Project proj = BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(),
								Project.class);
						proj.setId(((ObjectId) dbObject.get("_id")).toStringMongod());
						return proj;
					}
				});
		assertEquals(2, records.size());

		records = (List<Project>) mongoDbTemplate.findRecordsByMapValues("project",
				MapFactory.build(String.class, Object.class).toMap("createdBy=peishan"),
				new MongoCursorMapper<Project>() {

					@SuppressWarnings("unchecked")
					@Override
					public Project mapCursor(DBObject dbObject) {
						Project proj = BeanObjectGraphIntrospector.parseMapValuesToObject(dbObject.toMap(),
								Project.class);
						proj.setId(((ObjectId) dbObject.get("_id")).toStringMongod());
						return proj;
					}
				});
		assertEquals(0, records.size());
	}
}
