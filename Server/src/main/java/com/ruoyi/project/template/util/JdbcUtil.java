/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2012-4-6
 *******************************************************************************/

package com.ruoyi.project.template.util;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.project.template.commons.ColumnBean;
import com.ruoyi.project.template.commons.ColumnInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


@RefreshScope
@Component
public class JdbcUtil {
	//public static final String TABLE_SCHEMA = "ZHHCS";
	static DataSource dataSource = null;
	@Value("${spring.datasource.druid.master.url}")
	static String url = "";
	@Value("${spring.datasource.druid.master.password}")
	static String password = "";
	@Value("${spring.datasource.druid.master.username}")
	static String name = "";
	@Value("${spring.datasource.driverClassName}")
	static String driverName = "";

	static  Map<String,String> string=null;
	static Map<String,String> Date=null;
	static Map<String,String> Number=null;
	static Map<String,String> Clob=null;
	static Properties properties = new Properties();
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;
	/**
	 动态获取nacos配置中心参数
	 **/
    {
		try {
			System.out.println(url);
			System.out.println(name);
			System.out.println(password);
			System.out.println(driverName);
			Class.forName("com.alibaba.druid.pool.DruidDataSource");
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	public static Connection setInfo(String iUrl, String iName, String iPassword, String iDriver) {
		Connection con = null;
		try {
			url = iUrl;
			name = iName;
			password = iPassword;
			url = iUrl;
			String iDriverName = iDriver.equals("MySql") ? "com.mysql.cj.jdbc.Driver" : "oracle.jdbc.OracleDriver";
			Class.forName(iDriverName);
			con = DriverManager.getConnection(url, name, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, name, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static Connection getConnection(Properties pros) {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 关闭连接
	 * 
	 * @param rs
	 * @param stmt
	 * @param con
	 */
	public static void close(ResultSet rs, Statement stmt, Connection con) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取某SCHEMA下的某系统类里的所有表名称
	 *(用户表命名规则为：系统名称+表名.如O_NJZJWEB_T_ZLRZC_ZLJGL)
	 */
	public static List<String> getTablesInSchema(String tbSysName,String TABLE_SCHEMA) {
		List<String> retval = new ArrayList<String>();
		tbSysName = StringUtils.trimToNull(tbSysName);
		try {
			Connection connection = getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			String[] types = { "TABLE","VIEW" };
			ResultSet resultSet = dbmd.getTables(connection.getCatalog(),
					TABLE_SCHEMA,
					"%"
							+ (tbSysName == null ? "" : tbSysName.toUpperCase()
									+ "%"), types);
			// Get the table names
			while (resultSet.next()) {
				// Get the table name
				String tableName = resultSet.getString("TABLE_NAME");
				String tableSchema =
						"nsei_device".equals(TABLE_SCHEMA) ?
								resultSet.getString("TABLE_CAT") :
								resultSet.getString(2);

				if ((!tableName.contains("="))
						&& tableSchema.equals(TABLE_SCHEMA)) {
					retval.add(tableName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}
	/**
	 * 获取与系统名称符合的视图
	 * @param tbSysName
	 * @return
	 */
	public static List<String> getViewsInSchema(String tbSysName,String TABLE_SCHEMA) {
		List<String> retval = new ArrayList<String>();
		tbSysName = StringUtils.trimToNull(tbSysName);
		try {
			Connection connection = getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			String[] types = { "VIEW" };
			ResultSet resultSet = dbmd.getTables(connection.getCatalog(),
					TABLE_SCHEMA,
					"%"
							+ (tbSysName == null ? "" : tbSysName.toUpperCase()
									+ "%"), types);
			// Get the table names
			while (resultSet.next()) {
				// Get the table name
				String tableName = resultSet.getString("TABLE_NAME");
				String tableSchema = resultSet.getString(2);
				if ((!tableName.contains("="))
						&& tableSchema.equals(TABLE_SCHEMA)) {
					retval.add(tableName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	/**
	 * 查询数据库是否有某表
	 * 
	 * @param
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static boolean getAllTableName(String tableName) throws Exception {
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			DatabaseMetaData dbMetaData = conn.getMetaData();
			String[] types = { "TABLE" };
			rs = dbMetaData.getTables(null, null, tableName, types);
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			conn.close();
		}
		return false;
	}

	/**
	 * 为PreparedStatedment设置参数
	 * 
	 * @param table
	 *            表名
	 * @param sql
	 *            SQL语句（格式为SELECT * FROM XXX）
	 * @param pst
	 *            PreparedStatement
	 * @param params
	 *            参数为String-String类型
	 * @throws SQLException
	 */
	public static void setPreparedStatementParams(String table, String sql,
			PreparedStatement pst, Map<String, String> params)
			throws SQLException {
		if (params.size() > 0) {
			Map<String, Integer> paramIndexMap = new HashMap<String, Integer>();// paramname
																				// -
																				// index映射
			int paramIndex = 1;
			List<String> tableColumnNameList = getColumnMetaDataByTable(table);
			List<String> paramNameList = new ArrayList<String>();
			for (int i = 0; i < tableColumnNameList.size(); i++) {
				String tableColumnName = tableColumnNameList.get(i);
				String param = params.get(tableColumnName);
				if (param != null && !"".equals(param)) {

					paramIndexMap.put(tableColumnName, paramIndex);
					paramNameList.add(tableColumnName);
					paramIndex += 1;
				}
			}
			for (int i = 0; i < paramNameList.size(); i++) {
				try {
					pst.setString(paramIndexMap.get(paramNameList.get(i)),
							params.get(paramNameList.get(i)));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 通过参数拼接SQL
	 * 
	 * @param table
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static String getSQLByParams(String table, String sql,
			Map<String, String> params) throws SQLException {
		if (params.size() > 0) {
			int paramIndex = 1;
			List<String> tableColumnNameList = getColumnMetaDataByTable(table);
			for (int i = 0; i < tableColumnNameList.size(); i++) {
				String tableColumnName = tableColumnNameList.get(i);
				String param = params.get(tableColumnName);
				if (param != null && !"".equals(param)) {
					if (paramIndex == 1) {// 第一个查询条件
						sql += " where " + tableColumnName + " = ? ";
					} else {

						sql += " and " + tableColumnName + " = ? ";
					}

				}
			}
		}
		return sql;
	}

	/**
	 * 获取某表的字段映射关系（字段名-字段名）
	 * 
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getColumnMetaDataByTable(String table)
			throws SQLException {
		if (table == null || table.equals("")) {
			return null;
		}
		List<String> columnNameList = new ArrayList<String>();
		// 字段名-字段名，映射关系
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM " + table);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData(); // 获取字段名

			if (rsmd != null) {
				int count = rsmd.getColumnCount();
				for (int i = 1; i <= count; i++) {
					columnNameList.add(rsmd.getColumnName(i).toLowerCase());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rsmd = null;
			JdbcUtil.close(rs, pstmt, conn);
		}
		return columnNameList;
	}

	/**
	 * 获取列名对应注释及列名
	 * 
	 * @param tableName
	 * @return
	 */
	public static List<ColumnBean> getColumnBeansByTable(String tableName, String TABLE_SCHEMA) {
		List<ColumnBean> columnList = new ArrayList<ColumnBean>();
		if (!StringUtil.validateStringNotNull(tableName)) {
			return columnList;
		}
		string =new HashMap<>();
		Date =new HashMap<>();
		Number =new HashMap<>();
		Clob =new HashMap<>();
		string.put("name","字符串型");
		string.put("value","String");
		Date.put("name","日期型");
		Date.put("value","Date");
		Number.put("name","数字型");
		Number.put("value","Number");
		Clob.put("name","大字段型");
		Clob.put("value","Clob");
		List<Map<String,String>> type=new ArrayList<>();
		type.add(string);
		type.add(Date);
		type.add(Number);
		type.add(Clob);
		Connection conn = null;

		Properties props = new Properties();
		props.put("remarksReporting", "true");
		props.put("user", name);
		props.put("password", password);

		DatabaseMetaData dataBaseMetaData = null;
		ResultSet rs = null;
		try {
			conn = getConnection(props);

			dataBaseMetaData = conn.getMetaData();
			rs = dataBaseMetaData.getColumns(conn.getCatalog(), TABLE_SCHEMA,
					tableName.toUpperCase(), "%");
			System.out.print("rs:"+rs);
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				String columnRemarks = rs.getString("REMARKS");
				//System.out.print("columnName:"+columnName+"---columnRemarks:"+columnRemarks);
				ColumnBean columnBean = new ColumnBean(columnName,
						columnRemarks);// 注释
				columnBean.setType(type);
				columnList.add(columnBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, null, conn);
		}
		return columnList;
	}


	/**
	 * 获取列名对应注释及列名
	 *
	 * @param tableName
	 * @return
	 */
	public static List<JSONObject> getTableColumnBeansByTable(String tableName, String TABLE_SCHEMA) {
		List<JSONObject> columnList = new ArrayList<JSONObject>();
		if (!StringUtil.validateStringNotNull(tableName)) {
			return columnList;
		}

		Connection conn = null;

		Properties props = new Properties();
		props.put("remarksReporting", "true");
		props.put("user", name);
		props.put("password", password);

		DatabaseMetaData dataBaseMetaData = null;
		ResultSet rs = null;
		try {
			conn = getConnection(props);
			dataBaseMetaData = conn.getMetaData();
			rs = dataBaseMetaData.getColumns(conn.getCatalog(), TABLE_SCHEMA,
					tableName.toUpperCase(), "%");
			JSONObject jsonObject=null;
			while (rs.next()) {
				jsonObject=new JSONObject();
				String columnName = rs.getString("COLUMN_NAME");
				String columnRemarks = rs.getString("REMARKS");
				//System.out.print("columnName:"+columnName+"---columnRemarks:"+columnRemarks);
			jsonObject.put("title",columnRemarks);
			jsonObject.put("key",columnName);
			columnList.add(jsonObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, null, conn);
		}
		return columnList;
	}





	/**
	 *获取当前传入id的信息
	 *
	 * @param table 表名称
	 *          id   数据id
	 * @return
	 */
	public static JSONObject getTableByIdInfo(String table, String id){
		List<ColumnInfoBean> columnList = new ArrayList<ColumnInfoBean>();
		Connection connection = getConnection();
		String key=	getKey(table);
		Statement stmt=null;
		JSONObject jsonObject=new JSONObject();;
		ResultSet rs=null;
		try {
			stmt = connection.createStatement();
			 rs = stmt.executeQuery("SELECT * FROM " + table+" where "+key+"="+"'"+id+"'");
			System.out.println("SELECT * FROM " + table+" where "+key+"="+"'"+id+"'");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();
           while (rs.next()){
			// Get the column names; column indices start from 1
			for (int i = 1; i <= numColumns; i++) {
				// 字段名
				rsmd.getColumnName(i);
                //字段值
				rs.getObject(i);

				/*if(rsmd.getColumnTypeName(i).equals("DATE") && rs.getString(i)!=null ){

				}*/
				jsonObject.put(rsmd.getColumnName(i),rs.getString(i));
			}
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(rs, stmt, connection);
		}

		return jsonObject;
	}



	public static String getKey(String tableName)  {

		Connection conn = getConnection();
		DatabaseMetaData dataBaseMetaData = null;
		ResultSet rs = null;
		DatabaseMetaData dbmd = null;
		String	columnName="";
		try {
			dbmd = conn.getMetaData();
			rs  =  dbmd.getPrimaryKeys( null , null ,tableName.toUpperCase());
			while  (rs.next())  {
		     columnName=rs.getString(4);
				System.out.println(columnName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(rs, null, conn);
		}


		return columnName;
	}



	/**
	 * 取得某表下的所有字段信息
	 * 
	 * @param table
	 * @return
	 */
	public static List<ColumnInfoBean> getColumnInfoesInTable(String table) {
		List<ColumnInfoBean> columnList = new ArrayList<ColumnInfoBean>();
		Connection connection = null;

		// Create a result set
		Statement stmt = null;
		ResultSet rs=null;
		try {
		connection = getConnection();

			 stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + table);
			// Get result set meta data
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			// Get the column names; column indices start from 1
			for (int i = 1; i < numColumns + 1; i++) {
				ColumnInfoBean columnInfoBean = new ColumnInfoBean();

				// 字段名
				columnInfoBean.setName(rsmd.getColumnName(i));

				// 字段类型
				columnInfoBean.setTypeName(rsmd.getColumnTypeName(i));


				// 字段类型对应的java类名
				columnInfoBean.setClassName(rsmd.getColumnClassName(i));

				// 显示的长度
				columnInfoBean.setDisplaySize(String.valueOf(rsmd
						.getColumnDisplaySize(i)));

				// Precision
				columnInfoBean
						.setPrecision(String.valueOf(rsmd.getPrecision(i)));

				// Scale
				columnInfoBean.setScale(String.valueOf(rsmd.getScale(i)));
				columnList.add(columnInfoBean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(rs, stmt, connection);
		}

		return columnList;
	}

	/**
	 * 执行某段SQL语句
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public static void executeSql(String sql) throws SQLException {
		Connection conn = JdbcUtil.getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, statement, conn);
		}
	}


	public static String getResourceData(String tableName,List<String> fields){

		Connection conn = JdbcUtil.getConnection();
		Statement statement = null;
		try {
			if(fields.isEmpty() || StringUtils.isBlank(tableName)){
				return null;
			}

			String sql="select ";
			for (int i = 0; i < fields.size(); i++){

              sql+=i==fields.size()-1 ? fields.get(i): fields.get(i) +",";

			}
            sql+=" from "+ tableName;
			statement = conn.createStatement();
			System.out.println(sql);
			ResultSet resultSet = statement.executeQuery(sql);
			ResultSetMetaData resultSetMD = resultSet.getMetaData();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, statement, conn);
		}


		return null;
	}


	/**
	 * 计算数据表的总记录数
	 * 
	 * @return 总记录数
	 */
	public static int getTotalCount(String tbName) {
		int totalCount = 0;
		String sql = "select count(*) from " + tbName;
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			totalCount = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, stmt, conn);
		}
		return totalCount;
	}


	/**
	 * 计算数据表中某个字段为空的记录数
	 *
	 * @return
	 */
	public static int getTableFieldIsNullCount(String tbName,String field) {
		int totalCount = 0;
		String sql = "select count(*) AS  count from " + tbName+" where "+field+" is null or trim("+field+") is null";
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				totalCount = rs.getInt("count");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, stmt, conn);
		}
		return totalCount;
	}




	public static List<String> getTableNameList() throws SQLException {
		Connection connection = JdbcUtil.getConnection();

		List<String> tableNameList=new ArrayList<>();
		Statement stmt = connection.createStatement();
		String sql= "select table_name from all_tables a where a.OWNER = upper('"+name+"')";
		ResultSet resultSet = stmt.executeQuery(sql);
		while(resultSet.next()){//如果对象中有数据，就会循环打印出来
			tableNameList.add(resultSet.getString("TABLE_NAME"));
		}
	/*	List<Map<String,String>> tableNameList=new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		DatabaseMetaData dbmd=connection.getMetaData();
		ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
		while (resultSet.next()) {
			String tableName=resultSet.getString("TABLE_NAME");
			String remarkes = resultSet.getString("REMARKS");
			map.put(tableName,remarkes);
			tableNameList.add(map);
		}*/
		JdbcUtil.close(resultSet,stmt,connection);
		return tableNameList;
	}



}
