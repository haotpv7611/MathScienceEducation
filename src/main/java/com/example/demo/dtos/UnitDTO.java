package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UnitDTO{
	private long id;
	
	private int unitName;
	private long subjectId;
	private boolean isDisable;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdDate;
	private String createdBy;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime modifiedDate;
	private String description;
	
	 

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UnitDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUnitName() {
		return unitName;
	}

	public void setUnitName(int unitName) {
		this.unitName = unitName;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public UnitDTO(long id, int unitName, boolean isDisable, String description) {
		super();
		this.id = id;
		this.unitName = unitName;
		this.isDisable = isDisable;
		this.description = description;
	}

	
	
	

//	Comparator<UnitDTO> compareByUnitName = new Comparator<UnitDTO>() {
//		@Override
//		public int compare(UnitDTO unitDTO1, UnitDTO unitDTO2) {
//			if (unitDTO1.getUnitName() - unitDTO2.getUnitName() > 0 && )
//			return extractInt(unitDTO1.getUnitName()) - extractInt(o2.getUnitName());
//		}
//		UnitDTO extractInt(String s) {
//	        String num = s.replaceAll("\\D", "");
//	        // return 0 if no digits found
//	        return num.isEmpty() ? 0 : Integer.parseInt(num);
//	    }
//	};
//	
}
