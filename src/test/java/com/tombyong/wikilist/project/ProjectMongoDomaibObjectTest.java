package com.tombyong.wikilist.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.tombyong.database.mongodb.DomainObject;
import com.tombyong.database.mongodb.MongoDbTemplate;
import com.tombyong.util.MapFactory;

public class ProjectMongoDomaibObjectTest {
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
	public void testSaveProjectWithoutItems() {
		Project proj = new Project();
		proj.setName("moving house");
		proj.setCreatedBy("junyong");

		DomainObject<Project> o = DomainObject.forDomain(proj, "project", new String[] { "items" },
				mongoDb.getDB("test"));
		Project savedProject = o.save().fillId("id").get();
		assertNotNull(savedProject.getId());
		assertEquals("junyong", savedProject.getCreatedBy());
		assertEquals("moving house", savedProject.getName());
	}

	@Test
	public void testSaveAndRetrieveProjectWithoutItems() {
		Project proj = new Project();
		proj.setName("moving house");
		proj.setCreatedBy("junyong");

		DomainObject<Project> o = DomainObject.forDomain(proj, "project", new String[] { "items" },
				mongoDb.getDB("test"));
		Project savedProject = o.save().fillId("id").get();
		assertNotNull(savedProject.getId());
		assertEquals("junyong", savedProject.getCreatedBy());
		assertEquals("moving house", savedProject.getName());

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("_id", new ObjectId(savedProject.getId()));
		o = DomainObject.one("project", Project.class, conditions, mongoDb.getDB("test")).fillId("id");
		Project found = o.get();
		assertEquals(proj.getId(), found.getId());
	}

	@Test
	public void testSaveProjectWithItems() {
		Project proj = new Project();
		proj.setName("moving house");
		proj.setCreatedBy("junyong");

		Item item1 = new Item();
		item1.setName("car");

		Item item2 = new Item();
		item2.setName("furniture");

		proj.addItem(item1, item2);

		DomainObject<Project> o = DomainObject.forDomain(proj, "project", new String[] { "items" },
				mongoDb.getDB("test"));
		o.save().fillId("id");
		assertNotNull(proj.getId());

		Project savedProject = mongoDbTemplate.retrieveByPrimaryKey("project", proj.getId(), Project.class);
		assertEquals("moving house", savedProject.getName());
		assertEquals("junyong", savedProject.getCreatedBy());

		List<DomainObject<Item>> items = DomainObject.forDomains(proj.getItems(), mongoDb.getDB("test"));
		for (DomainObject<Item> each : items) {
			each.get().setBelongToProjectId(proj.getId());
			each.save().fillId("id");
			assertNotNull(each.get().getId());
		}

		List<DomainObject<Item>> savedItems = DomainObject.list("item", Item.class,
				MapFactory.build(String.class, String.class).toMap("belongToProjectId=" + proj.getId()),
				mongoDb.getDB("test"));

		assertEquals(2, savedItems.size());
		for (DomainObject<Item> each : savedItems) {
			assertNotNull(each.id());
			assertNull(each.get().getId());
			each.fillId("id");
		}
	}
}
