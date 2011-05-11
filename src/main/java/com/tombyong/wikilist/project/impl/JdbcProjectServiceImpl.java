package com.tombyong.wikilist.project.impl;

import java.util.Collection;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tombyong.wikilist.project.Item;
import com.tombyong.wikilist.project.Project;
import com.tombyong.wikilist.project.ProjectService;

public class JdbcProjectServiceImpl implements ProjectService {

	private JdbcTemplate jdbcTemplate;

	public JdbcProjectServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Project getProjectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Project> listAllProjects(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeProject(Project project) {
		// TODO Auto-generated method stub
	}

	@Override
	public Project saveProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project addOrUpdateItem(Project project, Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project removeItem(Project project, Item item) {
		// TODO Auto-generated method stub
		return null;
	}

}
