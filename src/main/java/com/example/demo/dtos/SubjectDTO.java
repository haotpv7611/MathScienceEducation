package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class SubjectDTO {

	private long id;
	@NotNull (message = "Subject Name Must Be Not Null !")
	@Length(max = 20, message = "Subject Name lenght must be less than 20 !")
	private String subjectName;
	@NotNull (message = "Subject Image Must Be Not Null !")
	private String imageUrl;
	private long gradeId;
	private boolean isDisable;
	@Length(message = "Description lenght must be less than 50 !")
	private String description;
	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SubjectDTO() {
		
	}

	public SubjectDTO(long id, String subjectName, long gradeId) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.gradeId = gradeId;
	}

	public SubjectDTO(long id, String subjectName, String imageUrl, long gradeId, boolean isDisable) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.imageUrl = imageUrl;
		this.gradeId = gradeId;
		this.isDisable = isDisable;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getGradeId() {
		return gradeId;
	}

	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	
	
	
}
