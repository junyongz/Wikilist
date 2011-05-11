package com.tombyong.wikilist.project.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.Mongo;
import com.tombyong.wikilist.project.Item;
import com.tombyong.wikilist.project.Project;

public class MongoDbProjectServiceImplTest {
	private MongoDbProjectServiceImpl service;

	private Mongo mongoDb;

	@Before
	public void setUp() throws UnknownHostException {
		mongoDb = new Mongo("127.0.0.1");
		service = new MongoDbProjectServiceImpl(mongoDb, "test");
	}

	@After
	public void destroy() throws UnknownHostException {
		Mongo mongoDb = new Mongo("127.0.0.1");
		mongoDb.getDB("test").getCollection("projects").drop();
		mongoDb.getDB("test").getCollection("items").drop();
	}

	@Test
	public void testSaveProjectIntoMongoDb() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		service.saveProject(project);
		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		Project saved = projects.iterator().next();
		assertEquals("tester", saved.getName());
		assertEquals("junyong", saved.getCreatedBy());
		assertEquals(todayDate, saved.getCreatedDate());
	}

	@Test
	public void testUpdateProjectIntoMongoDb() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		// create
		service.saveProject(project);

		Project savedProject = service.getProjectByPrimaryKey(project.getId());
		savedProject.setName("Haha");
		service.saveProject(project);
		
		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
	}

	@Test
	public void testSaveMoreProjectsIntoMongoDb() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		Project project2 = new Project();
		project2.setName("hello world");
		project2.setCreatedBy("junyong");
		project2.setCreatedDate(todayDate);

		service.saveProject(project);
		service.saveProject(project2);
		assertNotNull(project.getId());
		assertNotNull(project2.getId());

		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(2, projects.size());
	}

	@Test
	public void testSaveProjectWithOneItemIntoMongoDb() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		Item item = new Item();
		item.setName("vege");
		item.setCount(10);
		item.setDone(false);

		project.addItem(item);

		service.saveProject(project);
		assertNotNull(project.getId());
		assertNotNull(item.getId());

		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		project = service.getProjectByPrimaryKey(projects.iterator().next().getId());
		assertEquals(1, project.getItems().size());
	}

	@Test
	public void testSaveProjectWithMoreItemsIntoMongoDb() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		Item item = new Item();
		item.setName("vege");
		item.setCount(10);
		item.setDone(false);

		Item item2 = new Item();
		item2.setName("next stop");
		item2.setUrl("http://foursquare.com/venue/13gv2");
		item2.setDone(true);

		project.addItem(item);
		project.addItem(item2);

		service.saveProject(project);
		assertNotNull(project.getId());
		assertNotNull(item.getId());
		assertNotNull(item2.getId());

		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		project = service.getProjectByPrimaryKey(projects.iterator().next().getId());
		assertEquals(2, project.getItems().size());
	}

	@Test
	public void testSaveItemIntoExistingProject() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		service.saveProject(project);
		assertNotNull(project.getId());

		Item item = new Item();
		item.setName("vege");
		item.setCount(10);
		item.setDone(false);

		service.addOrUpdateItem(project, item);
		assertNotNull(item.getId());

		Item item2 = new Item();
		item2.setName("next stop");
		item2.setUrl("http://foursquare.com/venue/13gv2");
		item2.setDone(true);

		service.addOrUpdateItem(project, item2);
		assertNotNull(item2.getId());

		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		project = service.getProjectByPrimaryKey(projects.iterator().next().getId());
		assertEquals(2, project.getItems().size());
	}

	@Test
	public void testRemoveProjects() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		service.saveProject(project);
		assertNotNull(project.getId());

		Item item = new Item();
		item.setName("vege");
		item.setCount(10);
		item.setDone(false);

		service.addOrUpdateItem(project, item);
		assertNotNull(item.getId());

		Item item2 = new Item();
		item2.setName("next stop");
		item2.setUrl("http://foursquare.com/venue/13gv2");
		item2.setDone(true);

		service.addOrUpdateItem(project, item2);
		assertNotNull(item2.getId());

		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		project = service.getProjectByPrimaryKey(projects.iterator().next().getId());
		assertEquals(2, project.getItems().size());

		service.removeProject(project);

		projects = service.listAllProjects("junyong");
		assertEquals(0, projects.size());
		assertEquals(0, mongoDb.getDB("test").getCollection("items").count());
	}

	@Test
	public void testRemoveProjectItems() {
		Date todayDate = new Date();

		Project project = new Project();
		project.setName("tester");
		project.setCreatedBy("junyong");
		project.setCreatedDate(todayDate);

		service.saveProject(project);
		assertNotNull(project.getId());

		Item item = new Item();
		item.setName("vege");
		item.setCount(10);
		item.setDone(false);

		service.addOrUpdateItem(project, item);
		assertNotNull(item.getId());

		Item item2 = new Item();
		item2.setName("next stop");
		item2.setUrl("http://foursquare.com/venue/13gv2");
		item2.setDone(true);

		service.addOrUpdateItem(project, item2);
		assertNotNull(item2.getId());

		Collection<Project> projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		project = service.getProjectByPrimaryKey(projects.iterator().next().getId());
		assertEquals(2, project.getItems().size());

		service.removeItem(project, item2);

		assertEquals(1, project.getItems().size());

		projects = service.listAllProjects("junyong");
		assertEquals(1, projects.size());
		project = service.getProjectByPrimaryKey(projects.iterator().next().getId());
		assertEquals(1, project.getItems().size());
	}
}
