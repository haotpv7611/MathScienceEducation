package com.example.demo.utils;

import org.springframework.web.multipart.MultipartFile;

public class Util {
	public static String validateString(String property, int length, String errorMessage) {
		String error = "";
		if (property != null) {
			if (property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	public static String validateRequiredString(String property, int length, String errorMessage) {
		String error = "";
		if (property == null) {
			error = errorMessage;
		} else {
			if (property.isEmpty() || property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	public static String validateFile(MultipartFile file, String contentType, String errorMessage) {
		String error = "";
		if (file != null) {
			if (!file.getContentType().contains(contentType)) {
				error += errorMessage;
			}
		}

		return error;
	}

	public static String validateRequiredFile(MultipartFile file, String contentType, String errorMessage,
			String errorMessage2) {
		String error = "";
		if (file == null) {
			error = errorMessage;
		} else {
			if (file.isEmpty()) {
				error = errorMessage;
			} else if (!file.getContentType().contains(contentType)) {
				error += errorMessage2;
			}
		}

		return error;
	}
}
