package com.learningtimer.dataStoreObjects;

import java.util.Objects;

public class Project {

	private String projectName;

	public Project(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(projectName);
	}

	/*
	 * Two project is equal if the project name equals.
	 * */
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Project))
			return false;
		Project other = (Project) obj;
		return projectName.equals(other.projectName);
	}

	//just for testing
	@Override
	public String toString() {
		return "Project [projectName=" + projectName + "]";
	}

}
