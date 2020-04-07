package com.kayakwise.gray.api.util.dbentiry;

public class ColumnAttribute {
	// 字段名称
	private String columnName;
	// 字段类型
	private String columType;
	// 字段长度
	private int length;
	// 小数点后
	private int scale;
	// 是否为 nullable
	private int isNullable;
	// 是否为主键
	private boolean isPK;
	// 属性
	private String attributeName;
	// 属性类型
	private String attributeType;
	// 字段说明
	private String comment;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumType() {
		return columType;
	}

	public void setColumType(String columType) {
		this.columType = columType;
	}

	public int isNullable() {
		return isNullable;
	}

	public void setNullable(int isNullable) {
		this.isNullable = isNullable;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public int getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(int isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
