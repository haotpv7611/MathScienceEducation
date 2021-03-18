package com.example.demo.dtos;

public class UnitDTO{
	private long id;
	
//	private int unitName;
	private String unitName;
	private long subjectId;

	public UnitDTO() {
	}

	public UnitDTO(long id, String unitName, long subjectId) {
		super();
		this.id = id;
		this.unitName = unitName;
		this.subjectId = subjectId;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
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
