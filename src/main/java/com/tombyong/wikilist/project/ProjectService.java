package com.tombyong.wikilist.project;

import java.util.Collection;

public interface ProjectService {
	public Project getProjectByPrimaryKey(String id);

	/**
	 * Retrieve a list of project created by the user of provided username. This
	 * will only retrieve the basic info of project, not it's associated items.
	 * @param username username of the user
	 * @return list of project
	 */
	public Collection<Project> listAllProjects(String username);

	public void removeProject(Project project);

	public Project saveProject(Project project);

	public Project addOrUpdateItem(Project project, Item item);

	public Project removeItem(Project project, Item item);
}
