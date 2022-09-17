package projects;

import java.math.BigDecimal;
import java.util.*;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	
	ProjectService projectService = new ProjectService();
	
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project" + 
			"\n   2) List projects" + 
			"\n   3) Select a project" + 
			"\n   4) Update project details" + 
			"\n   5) Delete a project"
			);
	// @formatter:on
	
	
	private Scanner scanner = new Scanner(System.in);
	private Project curProject;
	

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}

	
	private void processUserSelections() {
		boolean done = false;
		while(!done) {
			try {
				int selection = getUserSelection();
				
				switch(selection) {
				
					case(-1):
						done =  exitMenu();
						break;
						
					case(1):
						createProject();
						break;		
						
					case(2):
						listProjects();
						break;
						
					case(3):
						selectProject();
						break;
						
					case(4):
						updateProjectDetails();
						break;
						
					case(5):
						deleteProject();		
						break;
						
					default:
						System.out.println("\n" + selection + " is not a valid selection. Try again.");
						break;
				}	
			}catch (Exception e){
				System.out.println("\nError: " + e + " Try again.");
				e.printStackTrace();
				
			}
		}
		
	}


	private void deleteProject() {
		listProjects();
		Integer projectToDelete = getIntInput("Enter the project ID to delete the project: ");
		try {
			projectService.deleteProject(projectToDelete);
			System.out.println("Project with ID " + projectToDelete + " has been deleted.");
			if(Objects.nonNull(curProject)) {
				if(curProject.getProjectId().equals(projectToDelete)) {
					curProject = null;
				}
			}
		}catch(DbException e){
			System.out.println("Project with ID " + projectToDelete + " does not exist.");
		}
		
		
		
		
	}


	private void updateProjectDetails() {
		if(Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project.");
		}else {
			Project project = new Project();
			
			String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
			project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
			
			BigDecimal estimatedHours = getDecimalInput("Enter the project estimated hours [" + curProject.getEstimatedHours() + "]");
			project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
			
			BigDecimal actualHours = getDecimalInput("Enter the project actual hours [" + curProject.getActualHours() + "]");
			project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
					
			Integer difficulty = getIntInput("Enter the project difficulty [" + curProject.getDifficulty() + "]");
			project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
					
			String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
			project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
			
			project.setProjectId(curProject.getProjectId());
			projectService.modifyProjectDetails(project);
			curProject = projectService.fetchProjectById(curProject.getProjectId());
		}
		
		
	}


	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		curProject = null;
		curProject = projectService.fetchProjectById(projectId);
	
		
	}


	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects: ");
		projects.forEach(project -> 
		System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
		
	}
	
	


	private void createProject() {
		Project project = new Project();
		String projectName = getStringInput("Enter project name");
		BigDecimal estimatedHours = getDecimalInput("Enter estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);

		System.out.println("You successfully created project " + dbProject);
		
		
	}


	private BigDecimal getDecimalInput(String prompt) {

		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
			
		}catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number");
		}
	}


	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}


	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
	}
	
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		operations.forEach(line -> System.out.println("   " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}


	private Integer getIntInput(String prompt) {
		
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		
		try {
			return Integer.valueOf(input);
			
		}catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number");
		}		
	}


	private String getStringInput(String prompt) {
		System.out.printf(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}
	
	
	
	
	


	

}
