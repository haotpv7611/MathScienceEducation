package com.example.demo.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Classes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String className;

	private String status;

	@ManyToOne
	@JoinColumn(name = "schoolGradeId")
	private SchoolGrade schoolGrade;

	@OneToMany(mappedBy = "classes")
	private List<StudentProfile> studentProfileList;

	@CreatedDate
	private LocalDateTime createdDate;
//	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	private String modifiedBy;

	@PrePersist
	public void onCreate() {
		this.createdDate = LocalDateTime.now(ZoneId.of("UTC+7"));
		this.modifiedDate = LocalDateTime.now(ZoneId.of("UTC+7"));
	}

	@PreUpdate
	public void onUpdate() {
		this.modifiedDate = LocalDateTime.now(ZoneId.of("UTC+7"));
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the schoolGrade
	 */
	public SchoolGrade getSchoolGrade() {
		return schoolGrade;
	}

	/**
	 * @param schoolGrade the schoolGrade to set
	 */
	public void setSchoolGrade(SchoolGrade schoolGrade) {
		this.schoolGrade = schoolGrade;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the studentProfileList
	 */
	public List<StudentProfile> getStudentProfileList() {
		return studentProfileList;
	}

	/**
	 * @param studentProfileList the studentProfileList to set
	 */
	public void setStudentProfileList(List<StudentProfile> studentProfileList) {
		this.studentProfileList = studentProfileList;
	}

}
