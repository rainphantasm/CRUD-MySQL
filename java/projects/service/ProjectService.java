package projects.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.DbConnection;
import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
	
	ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {	
		return projectDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException(
						"Project with project ID= " + projectId + " does not exist."));

	}
	

	public void modifyProjectDetails(Project project) {
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
		}
		
	}

	public void deleteProject(Integer projectToDelete) {
		if(!projectDao.deleteProject(projectToDelete)) {
			throw new DbException("Project with ID=" + projectToDelete + " does not exist.");
		}
	}

	

}
