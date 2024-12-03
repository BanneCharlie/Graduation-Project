package com.ruoyi.project.template.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColumnBean {
	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 列名对应注释
	 */
	private String columnRemark;


private 	List<Map<String,String>> type=new ArrayList<>();

	public ColumnBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ColumnBean(String columnName, String columnRemark) {
		super();
		this.columnName = columnName;
		this.columnRemark = columnRemark;

	}

	public List<Map<String, String>> getType() {
		return type;
	}

	public void setType(List<Map<String, String>> type) {
		this.type = type;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnRemark() {
		return columnRemark;
	}

	public void setColumnRemark(String columnRemark) {
		this.columnRemark = columnRemark;
	}

}
