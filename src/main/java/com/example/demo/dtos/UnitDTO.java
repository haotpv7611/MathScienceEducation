package com.example.demo.dtos;

public class UnitDTO{
	private long id;
	
	private int unitName;
	private long subjectId;

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

	public UnitDTO(long id, int unitName, long subjectId) {
		super();
		this.id = id;
		this.unitName = unitName;
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
