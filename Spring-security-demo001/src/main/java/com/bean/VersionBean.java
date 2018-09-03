package com.bean;



import org.springframework.beans.factory.annotation.Value;

public class VersionBean {
	@Value("${version}")
	private String version;
	@Value("${projectName}")
	private String projectName;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	
}
