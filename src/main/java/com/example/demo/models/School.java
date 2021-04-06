package com.example.demo.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

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
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String schoolName;
	private String schoolStreet;
	private String schoolDistrict;
	private String schoolCode;
	private int schoolCount;
	private String status;

	@OneToMany(mappedBy = "school")
	private Set<SchoolGrade> schoolGrade;

	@ManyToOne
	@JoinColumn(name = "schoolLevelId")
	private SchoolLevel schoolLevel;

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
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	/**
	 * @return the schoolStreet
	 */
	public String getSchoolStreet() {
		return schoolStreet;
	}

	/**
	 * @param schoolStreet the schoolStreet to set
	 */
	public void setSchoolStreet(String schoolStreet) {
		this.schoolStreet = schoolStreet;
	}

	/**
	 * @return the schoolDistrict
	 */
	public String getSchoolDistrict() {
		return schoolDistrict;
	}

	/**
	 * @param schoolDistrict the schoolDistrict to set
	 */
	public void setSchoolDistrict(String schoolDistrict) {
		this.schoolDistrict = schoolDistrict;
	}

	/**
	 * @return the schoolCode
	 */
	public String getSchoolCode() {
		return schoolCode;
	}

	/**
	 * @param schoolCode the schoolCode to set
	 */
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	/**
	 * @return the schoolCount
	 */
	public int getSchoolCount() {
		return schoolCount;
	}

	/**
	 * @param schoolCount the schoolCount to set
	 */
	public void setSchoolCount(int schoolCount) {
		this.schoolCount = schoolCount;
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
	 * @return the id
	 */
	public long getId() {
		return id;
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
	 * @return the schoolGrade
	 */
	public Set<SchoolGrade> getSchoolGrade() {
		return schoolGrade;
	}

	/**
	 * @param schoolGrade the schoolGrade to set
	 */
	public void setSchoolGrade(Set<SchoolGrade> schoolGrade) {
		this.schoolGrade = schoolGrade;
	}

	/**
	 * @return the schoolLevel
	 */
	public SchoolLevel getSchoolLevel() {
		return schoolLevel;
	}

	/**
	 * @param schoolLevel the schoolLevel to set
	 */
	public void setSchoolLevel(SchoolLevel schoolLevel) {
		this.schoolLevel = schoolLevel;
	}

}
