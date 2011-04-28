package edu.unlv.cs.rebelhotel.file;

public class FileUploadException extends RuntimeException {
	public FileUploadException(String message, Throwable throwable) {
		super(message,throwable);
	}
}
