package com.example.demo.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class SchoolGrade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
//	private long gradeId;
//	private long schoolId;
	@CreatedDate
	private LocalDateTime createdDate;
//	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private LocalDateTime modifiedDate;
//	@LastModifiedBy
	private String modifiedBy;
	private boolean isDisable;

	@ManyToOne
	@JoinColumn(name = "schoolId")
	School school;
	@ManyToOne
	@JoinColumn(name = "gradeId")
	Grade grade;

	@OneToMany(mappedBy = "schoolGrade")
	private List<Class> classList;

	/**
	 * @return the grade
	 */
	public Grade getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	/**
	 * @return the school
	 */
	public School getSchool() {
		return school;
	}

	/**
	 * @param school the school to set
	 */
	public void setSchool(School school) {
		this.school = school;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the isDisable
	 */
	public boolean isDisable() {
		return isDisable;
	}

	/**
	 * @param isDisable the isDisable to set
	 */
	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the classList
	 */
	public List<Class> getClassList() {
		return classList;
	}

	/**
	 * @param classList the classList to set
	 */
	public void setClassList(List<Class> classList) {
		this.classList = classList;
	}

//	/**
//	 * @return the gradeId
//	 */
//	public long getGradeId() {
//		return gradeId;
//	}
//
//	/**
//	 * @param gradeId the gradeId to set
//	 */
//	public void setGradeId(long gradeId) {
//		this.gradeId = gradeId;
//	}
//
//	/**
//	 * @return the schoolId
//	 */
//	public long getSchoolId() {
//		return schoolId;
//	}
//
//	/**
//	 * @param schoolId the schoolId to set
//	 */
//	public void setSchoolId(long schoolId) {
//		this.schoolId = schoolId;
//	}

}
