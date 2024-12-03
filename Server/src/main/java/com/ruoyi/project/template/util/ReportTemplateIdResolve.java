package com.ruoyi.project.template.util;


import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.domain.ReqContReview;
import com.ruoyi.project.template.domain.DataSourceConfig;
import com.ruoyi.project.template.domain.ReportTemplate;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * @author niminui
 * @date 2020/11/9 11:55
 */
@SuppressWarnings("all")
public class ReportTemplateIdResolve {

    private static final ThreadLocal<Map<String,String>> MAP_THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);
    /**
     * 根据报告模板中存的内容 ${test}，解析出对应的 sql 语句并并执行，将结果替换到原内容对应处
     *
     * @param entity 报告模板实体类
     * @return
     */
    public static ReportTemplate sqlTemplateResolve(ReportTemplate entity, DataSourceConfig dataSource) {
        JdbcUtil.setInfo(dataSource.getJdbcUrl(), dataSource.getJdbcUserName(), dataSource.getJdbcPwd(), dataSource.getJdbcDriverType());
        String tempOriginal = entity.getTemplateContent();
        Map<String, String> tableField = new HashMap<>();
        //首先将表名、字段名、条件存入map，表名为map的key，map的value则为查询条件的key1='value1';key2='value2'
        for (int i = 0; i < tempOriginal.length(); i++) {
            int start = tempOriginal.indexOf("${");
            if (start >= 0) {
                int end = tempOriginal.indexOf("}");
                String[] temp = tempOriginal.substring(start + 2, end)
                        .replaceAll("&#39;", "'")
                        .split("\\.");
                tempOriginal = tempOriginal.substring(end + 1);
                if (tableField.containsKey(temp[0])) {
                    tableField.put(temp[0], tableField.get(temp[0]) + ";" + temp[1]);
                } else {
                    tableField.put(temp[0], temp[1]);
                }
                i = 0;
            } else {
                //若在content中没有找到 ${ ，则说明用户没有配置动态查询
                break;
            }
        }

        /**
         * 对上一步保存的 map 进行遍历，拼装sql
         * 对每一个表都拼接出一个 sql 语句并执行，然后将原 content 替换为查询出的结果
         * ${RS_COUNT_FUN(BASE_MENU.MENU_NAME='管理')}
         * ${DATA_SOURCE_CONFIG.DATA_SOURCE_NAME='Oracle'}
         */
        for (Map.Entry<String, String> entry : tableField.entrySet()) { //对表的循环
            StringBuilder sql = new StringBuilder();
            String tableName = entry.getKey();
            boolean isCount = false;
            if (tableName.contains("RS_COUNT_FUN")) {
                isCount = true;
                tableName = tableName.replaceAll("RS_COUNT_FUN\\(", "");
            }
            for (String where : entry.getValue().split(";")) { //对一个表下的所有查询条件循环
                if (isCount) {
                    where = where.replaceAll("\\)", "");
                }
                sql.append("select ")
                        .append(tableName).append(".").append(where.split("=")[0])
                        .append(" from ").append(tableName)
                        .append(" where ").append(tableName).append(".").append(where);
                ;
                Map<String, List<String>> result = ReportTemplateIdResolve.queryBySql(sql.toString());
                for (Map.Entry<String, List<String>> res : result.entrySet()) {
                    String key = !isCount ? "${" + entry.getKey() + "." + where.replaceAll("'", "&#39;") + "}"
                            : "${" + entry.getKey() + "." + where.replaceAll("'", "&#39;") + ")}";
                    List<String> values = res.getValue();
                    String value = (values == null || values.size() == 0)
                            ? "暂无"
                            : (isCount ? String.valueOf(res.getValue().size()) : res.getValue().get(0));
                    entity.setTemplateContent(entity.getTemplateContent().replace(key, value));
                }
                sql.setLength(0);
            }
            isCount = false;

        }
        return entity;
    }

    /**
     * 执行sql，并将结果转为 Map<String, List<String>>
     *
     * @param sql 需要运行的sql
     * @return
     */
    public static Map<String, List<String>> queryBySql(String sql) {
        Map<String, List<String>> handle = new HashMap<>();
        Connection conn = JdbcUtil.getConnection();
        ResultSet resultSet = null;
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            resultSet = pst.executeQuery();
            handle = handle(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(null, pst, conn);
        }
        return handle;
    }

    /**
     * 将 JDBC 查询的结果集 ResultSet转为 map
     *
     * @param set JDBC 查询的结果集
     * @return
     * @throws SQLException
     */
    private static Map<String, List<String>> handle(ResultSet set) throws SQLException {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        ResultSetMetaData rsmd = set.getMetaData();
        int count = rsmd.getColumnCount();

        List<String>[] lists = new List[count];

        for (int i = 0; i < lists.length; i++) {
            lists[i] = new ArrayList<String>();
            map.put(rsmd.getColumnName(i + 1), lists[i]);
        }
        while (set.next()) {
            for (int i = 0; i < lists.length; i++) {
                lists[i].add(set.getString(i + 1));
            }
        }
        return map;
    }

    /**
     * 解析html输出为word
     *
     * @param request  request
     * @param response response
     * @param content  html内容
     * @param path     输出路径
     * @return
     * @throws Exception
     */
    public static String exportWord(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String content,
                                    String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString().substring(0, 13) + "-" + System.currentTimeMillis() + ".doc";
        byte b[] = content.getBytes("utf-8");  //这里是必须要设置编码的，不然导出中文就会乱码。
        ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中
        //生成word
        POIFSFileSystem poifs = new POIFSFileSystem();
        DirectoryEntry directory = poifs.getRoot();
        DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
        //输出文件
        response.setCharacterEncoding("utf-8");
        //设置word格式
        response.setContentType("application/msword");
        response.setHeader("Content-disposition", "attachment;filename=exportWord.docx");
        FileOutputStream ostream = new FileOutputStream(path + fileName);
        poifs.writeFilesystem(ostream);
        bais.close();
        ostream.close();
        return fileName;
    }

    /**
     * 解析并添加报告模板中由检验流程传过来的值 客户端标记的 ${xxx} 字段会被解析
     *
     * @param entity 报告模板实体类
     * @param reqRep
     * @return
     */
    public static String resolveByReqCheck(String srcContent, ReqContReview reqContReview, String deviceNumber) {
        String content = srcContent;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(content)) {
            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) == '$' && content.charAt(i + 1) == '{') {
                    int start = content.indexOf("${");
                    int end = content.indexOf("}");
                    String fieldName = content.substring(start + 2, end);
                    Object fieldVal = "deviceNumber".equals(fieldName) ? deviceNumber : getFieldValueByName(fieldName, reqContReview);
                    if (fieldVal instanceof Date) {
                        fieldVal = sdf.format(fieldVal);
                    }
                    content = content.replaceAll("\\$\\{" + fieldName + "}", Objects.isNull(fieldVal) ? "" : fieldVal.toString());
                    i = end;
                }
            }
        }
        return content;
    }

    public static BusinessRequestReport resolveReportInfo(BusinessRequestReport reportInstance,ReqContReview reqContReview){

        Map<String, String> localParamMap = MAP_THREAD_LOCAL.get();
        localParamMap.put("deviceNumber" , reportInstance.getBusinessDeviceNumber());
        String reportGenericContext = reportInstance.getReportGenericContext();
        if (StringUtils.isNotEmpty(reportGenericContext)){
            reportGenericContext = replacePlaceHolderValue(reportGenericContext,reqContReview);
            reportInstance.setReportGenericContext(reportGenericContext);
        }

        String reportTemplateContext = reportInstance.getReportTemplateContext();
        if (StringUtils.isNotEmpty(reportTemplateContext)){
            reportTemplateContext = replacePlaceHolderValue(reportTemplateContext,reqContReview);
            reportInstance.setReportTemplateContext(reportTemplateContext);
        }

        MAP_THREAD_LOCAL.remove();

        return reportInstance;
    }

    private static String replacePlaceHolderValue(String sourceContent , Object originalObj){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < sourceContent.length(); i++) {
            if (sourceContent.charAt(i) == '$' && sourceContent.charAt(i + 1) == '{') {
                int start = sourceContent.indexOf("${");
                int end = sourceContent.indexOf("}");
                String fieldName = sourceContent.substring(start + 2, end);
                Object fieldVal = getFieldValueByName(fieldName, originalObj);
                if (fieldVal == null){
                    fieldVal = MAP_THREAD_LOCAL.get().get(fieldVal);
                }
                else if (fieldVal instanceof Date) {
                    fieldVal = sdf.format(fieldVal);
                }
                sourceContent = sourceContent.replaceAll("\\$\\{" + fieldName + "}", Objects.isNull(fieldVal) ? "" : fieldVal.toString());
                i = end;
            }
        }

        return sourceContent;
    }

    private static Object getFieldValueByName(String fieldName, Object obj) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
