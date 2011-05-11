package com.tombyong.wikilist.project.web;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tombyong.wikilist.project.Project;
import com.tombyong.wikilist.project.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private MessageCodesResolver messageCodesResolver;

	@InitBinder("project")
	protected void initBinder(WebDataBinder binder) {
		binder.setRequiredFields("name");
		binder.setMessageCodesResolver(this.messageCodesResolver);
	}

	@RequestMapping("/new")
	public String newEmptyProject(Model model) {
		model.addAttribute("project", new Project());
		return "/project/create";
	}

	@RequestMapping("/edit/{projectId}")
	public String editProject(@PathVariable String projectId, Model model) {
		model.addAttribute("project", projectService.getProjectByPrimaryKey(projectId));
		return "/project/create";
	}

	@RequestMapping("/save")
	public String saveProject(@ModelAttribute("project") Project project, BindingResult result, Model model,
			Principal user) {
		if (result.hasErrors()) {
			return "/project/create";
		}

		// need to know which properties shouldn't be copied over, ie, editable
		// field in edit page.
		if (StringUtils.hasLength(project.getId())) {
			Project proj = projectService.getProjectByPrimaryKey(project.getId());
			BeanUtils.copyProperties(proj, project, new String[] { "name" });
			project.setModifiedBy(user.getName());
			project.setModifiedDate(new Date());
		}
		else {
			project.setCreatedBy(user.getName());
			project.setCreatedDate(new Date());
		}

		projectService.saveProject(project);

		return "redirect:/projects/";
	}

	@RequestMapping("/remove/{projectId}")
	public String removeProject(@PathVariable String projectId) {
		Project project = projectService.getProjectByPrimaryKey(projectId);
		if (project != null) {
			projectService.removeProject(project);
		}

		return "redirect:/projects/";
	}

	@RequestMapping("/")
	public String listProjects(Model model, Principal user) {
		model.addAttribute("projects", this.projectService.listAllProjects(user.getName()));
		return "/project/list";
	}
}
