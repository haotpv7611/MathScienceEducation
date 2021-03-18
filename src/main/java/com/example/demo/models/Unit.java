package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Unit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int unitName;
//	private String unitName;
	private long subjectId;
	private boolean isDisable;

	/**
	 * @return the unitName
	 */
	public int getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(int unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
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
}
