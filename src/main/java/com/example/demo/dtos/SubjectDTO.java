package com.example.demo.dtos;

public class SubjectDTO {

	private Long id;
	private String subjectName;
	private String imageUrl;
	private Long gradeId;
	private boolean isDisable;
	
	public SubjectDTO() {
		
	}

	public SubjectDTO(Long id, String subjectName, String imageUrl, Long gradeId, boolean isDisable) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.imageUrl = imageUrl;
		this.gradeId = gradeId;
		this.isDisable = isDisable;
	}

	public SubjectDTO(Long id, String subjectName, Long gradeId) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.gradeId = gradeId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}
	
	
	
}
