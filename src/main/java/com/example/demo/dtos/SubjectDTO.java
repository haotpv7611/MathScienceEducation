package com.example.demo.dtos;

public class SubjectDTO {

	private long id;
	private String subjectName;
	private String imageUrl;
	private long gradeId;
	private boolean isDisable;
	
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
