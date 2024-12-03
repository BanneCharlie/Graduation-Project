package com.ruoyi.project.template.commons;

public class ColumnInfoBean {
	private String name;
	private String typeName;
	private String className;
	private String displaySize;
	private String precision;
	private String scale;

	public ColumnInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ColumnInfoBean(String name, String typeName, String className,
                          String displaySize, String precision, String scale) {
		super();
		this.name = name;
		this.typeName = typeName;
		this.className = className;
		this.displaySize = displaySize;
		this.precision = precision;
		this.scale = scale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

}
