package com.example.demo.dtos;

import java.util.List;

public class OptionQuestionChooseDTO {
	private long id;
	private String optionText;
	private List<String> optionImageUrlList;

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
	 * @return the optionText
	 */
	public String getOptionText() {
		return optionText;
	}

	/**
	 * @param optionText the optionText to set
	 */
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	/**
	 * @return the optionImageUrlList
	 */
	public List<String> getOptionImageUrlList() {
		return optionImageUrlList;
	}

	/**
	 * @param optionImageUrlList the optionImageUrlList to set
	 */
	public void setOptionImageUrlList(List<String> optionImageUrlList) {
		this.optionImageUrlList = optionImageUrlList;
	}

}
