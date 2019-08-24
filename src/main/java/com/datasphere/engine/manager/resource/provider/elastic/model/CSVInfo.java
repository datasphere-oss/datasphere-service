package com.datasphere.engine.manager.resource.provider.elastic.model;

public class CSVInfo {
	private String location;
	private String type;
	private String comment;
	private String escape;
	private boolean extractHeader;
	private String fieldDelimiter;
	private String lineDelimiter;
	private String quote;
	private boolean skipFirstLine;
	private boolean trimHeader;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEscape() {
		return escape;
	}

	public void setEscape(String escape) {
		this.escape = escape;
	}

	public boolean isExtractHeader() {
		return extractHeader;
	}

	public void setExtractHeader(boolean extractHeader) {
		this.extractHeader = extractHeader;
	}

	public String getFieldDelimiter() {
		return fieldDelimiter;
	}

	public void setFieldDelimiter(String fieldDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
	}

	public String getLineDelimiter() {
		return lineDelimiter;
	}

	public void setLineDelimiter(String lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public boolean isSkipFirstLine() {
		return skipFirstLine;
	}

	public void setSkipFirstLine(boolean skipFirstLine) {
		this.skipFirstLine = skipFirstLine;
	}

	public boolean isTrimHeader() {
		return trimHeader;
	}

	public void setTrimHeader(boolean trimHeader) {
		this.trimHeader = trimHeader;
	}
}
