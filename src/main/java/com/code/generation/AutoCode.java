package com.code.generation;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.*;

public class AutoCode {


    private static Connection connection;
    private static Statement statement;
    private static String tableSchema;

    private static Boolean enableBaseEntity = false;
    private static String[] baseEntityClons;
    private static LinkedList<ColumnInfo> baseColumnInfoList = new LinkedList();

    //数据库连接
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/myapp?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static String NAME = "root";
    private static String PASS = "ZIjing123";
    private static String AUTHOR = "HJW";
    //输出实体类的包
    private static String PKG = "com.starter.app.entity";
    private static String PKG_ENTITY = "entity";
    private static String PKG_DTO = "dto";
    private static String PKG_VO = "vo";
    private static String PKG_MAPPER = "mapper";
    private static String PKG_SERVICE = "service";
    private static String PKG_SERVICEIMPL = "service.impl";
    private static String PKG_CONTROLLER = "controller";

    private static Boolean GEN_SERVICE = true;
    private static Boolean GEN_MAPPER = true;
    private static Boolean GEN_CONTROLLER = false;
    private static Boolean OVER_WRITE = false;

    //表名
    private static String[] TABS = {};
    //表前缀[为空时输出库中所有表] (生成实体类类名时会去除)
    private static String[] TAB_PRE = {"t_"};
    private static String SHOW_COLUMN_INFO = "select column_name,column_comment,data_type,column_key from information_schema.columns where table_name = '${tableName}' ";
    private final String REPLACE_TABLE_NAME = "${tableName}";


    private final String SOURCE_PATH = new File("").getAbsolutePath() + "/src/main/java/";
    private final String MAPPER_PATH = new File("").getAbsolutePath() + "/src/main/resources/";
    private static String templatePath = AutoCode.class.getClassLoader().getResource("templates").getPath();
    private static FreemarkerUtil mFreemarker = FreemarkerUtil.getInstance();

    public static AutoCode init() {
        return new AutoCode();
    }


    /**
     * 数据连接配置
     * @param url
     * @param username
     * @param password
     * @return
     */
    public AutoCode initDB(String url, String username, String password) {
        URL = url;
        NAME = username;
        PASS = password;
        return this;
    }

    /**
     * 生成代码的表及代码生成路径
     * @param tables
     * @param tabPres
     * @param basePackage
     * @return
     */
    public AutoCode tableInfo(String[] tables, String[] tabPres,String basePackage) {
        TABS = tables;
        TAB_PRE = tabPres;
        PKG = basePackage;
        return this;
    }

    /**
     * 实体基类使用
     * @param clns
     * @return
     */
    public AutoCode baseEntityInfo(String... clns) {
        if (null != clns && clns.length > 0) {
            baseEntityClons = clns;
            enableBaseEntity = true;
        }
        return this;
    }

    /**
     * 是否生成service 及实现类
     * @param genService
     * @return
     */
    public AutoCode genService(Boolean genService) {
        this.GEN_SERVICE = genService;
        return this;
    }
    /**
     * 是否生成控制器
     * @param genConroller
     * @return
     */
    public AutoCode genController(Boolean genConroller) {
        this.GEN_CONTROLLER = genConroller;
        return this;
    }


    /**
     * 是否生成mapper 和 mapper.xml
     * @param genMapper
     * @return
     */
    public AutoCode genMapper(Boolean genMapper) {
        this.GEN_MAPPER = genMapper;
        return this;
    }

    /**
     * 是否可覆盖
     * @param overWrite
     * @return
     */
    public AutoCode overWrite(boolean overWrite) {
        this.OVER_WRITE = overWrite;
        return this;
    }


    public void genCode() throws Exception {
        //连接数据库
        connect();
        if (null == TABS || TABS.length < 1) {
            System.out.println("请设置表名");
            return;
        }

        for (int i=0;i<TABS.length;i++) {
            TableInfo tableInfo = tableInfo(TABS[i]);
            if (enableBaseEntity && i==0) {
                genBaseEntity();
            }
            generateEntity(tableInfo);
            generateDto(tableInfo);
            generateVo(tableInfo);

            if(GEN_SERVICE){
                generateService(tableInfo);
                generateServiceImpl(tableInfo);
            }
            if(GEN_MAPPER){
                generateMapper(tableInfo);
                generateMapperXML(tableInfo);
            }
            if(GEN_CONTROLLER){
                generateController(tableInfo);
            }

        }


    }

    private void generateVo(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_VO;
        Map map = new HashMap();
        map.put("table", tableInfo);
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment()+"响应对象");
        map.put("voPackage", PKG + "." + PKG_VO);
        map.put("dtoPackage", PKG + "." + PKG_DTO);
        String fileName = tableInfo.getUpClassName()+"VO";
        map.put("className", fileName);
        mFreemarker.tempWriter(templatePath, "vo.ftl", packageName, fileName + ".java", map);
    }

    private void generateDto(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_DTO;
        Map map = new HashMap();
        map.put("table", tableInfo);
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment()+"请求对象");
        map.put("dtoPackage", PKG + "." + PKG_DTO);
        String fileName = tableInfo.getUpClassName()+"DTO";
        map.put("className", fileName);
        mFreemarker.tempWriter(templatePath, "dto.ftl", packageName, fileName + ".java", map);
    }


    private void genBaseEntity() throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_ENTITY;
        Map map = new HashMap();
        map.put("commonClns", baseColumnInfoList);
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", "实体基类");
        map.put("entityPackage", PKG + "." + PKG_ENTITY);
        String fileName = "BaseEntity";
        map.put("className", fileName);
        mFreemarker.overWrite(OVER_WRITE).tempWriter(templatePath, "base_entity.ftl", packageName, fileName + ".java", map);
    }


    private void generateEntity(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_ENTITY;
        Map map = new HashMap();
        map.put("table", tableInfo);
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment());
        map.put("entityPackage", PKG + "." + PKG_ENTITY);
        String fileName = tableInfo.getUpClassName();
        map.put("className", tableInfo.getUpClassName());
        map.put("enableBaseEntity", enableBaseEntity);
        map.put("baseEntityName", "BaseEntity");
        mFreemarker.tempWriter(templatePath, "entity.ftl", packageName, fileName + ".java", map);
    }

    private void generateMapper(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_MAPPER;
        Map map = new HashMap();
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment());
        map.put("mapperPackage", PKG + "." + PKG_MAPPER);
        map.put("entityPackage", PKG + "." + PKG_ENTITY);
        String fileName = tableInfo.getUpClassName() + "Mapper";
        map.put("entityName", tableInfo.getUpClassName());
        map.put("className", fileName);
        mFreemarker.tempWriter(templatePath, "mapper.ftl", packageName, fileName + ".java", map);
    }

    private void generateService(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_SERVICE;
        Map map = new HashMap();
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment());
        map.put("servicePackage", PKG + "." + PKG_SERVICE);
        map.put("entityPackage", PKG + "." + PKG_ENTITY);
        String fileName = tableInfo.getUpClassName() + "Service";
        map.put("entityName", tableInfo.getUpClassName());
        map.put("className", fileName);
        mFreemarker.tempWriter(templatePath, "service.ftl", packageName, fileName + ".java", map);
    }

    private void generateServiceImpl(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_SERVICEIMPL;
        Map map = new HashMap();
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment());
        map.put("serviceImplPackage", PKG + "." + PKG_SERVICEIMPL);
        map.put("mapperPackage", PKG + "." + PKG_MAPPER);
        map.put("entityPackage", PKG + "." + PKG_ENTITY);
        map.put("servicePackage", PKG + "." + PKG_SERVICE);
        String serviceName = tableInfo.getUpClassName() + "Service";
        String fileName = serviceName + "Impl";
        map.put("serviceName", serviceName);
        String entityMapper = tableInfo.getUpClassName() + "Mapper";
        map.put("entityMapper", entityMapper);
        map.put("entityName", tableInfo.getUpClassName());
        map.put("className", fileName);
        mFreemarker.tempWriter(templatePath, "serviceImpl.ftl", packageName, fileName + ".java", map);
    }


    private void generateController(TableInfo tableInfo) throws Exception {
        String packageName = SOURCE_PATH + PKG.replace(".", File.separator) + "/" + PKG_CONTROLLER;
        Map map = new HashMap();
        map.put("author", AUTHOR);
        map.put("nowDate", new Date());
        map.put("desc", tableInfo.getClassComment()+"控制器");
        map.put("controllerPackage", PKG + "." + PKG_CONTROLLER);
        map.put("servicePackage", PKG + "." + PKG_SERVICE);
        String serviceName = tableInfo.getLowerClassName() + "Service";
        map.put("serviceName", serviceName);
        String fileName = tableInfo.getUpClassName() + "Controller";
        map.put("entityName", tableInfo.getLowerClassName());
        map.put("className", fileName);
        mFreemarker.tempWriter(templatePath, "controller.ftl", packageName, fileName + ".java", map);
    }

    private void generateMapperXML(TableInfo tableInfo) throws Exception {
        String packageName = MAPPER_PATH + "/" + PKG_MAPPER;
        Map map = new HashMap();
        map.put("table", tableInfo);
        map.put("mapperPackage", PKG + "." + PKG_MAPPER);
        map.put("entityPackage", PKG + "." + PKG_ENTITY);
        String fileName = tableInfo.getUpClassName() + "Mapper";
        String entityMapper = tableInfo.getUpClassName() + "Mapper";
        map.put("entityMapper", entityMapper);
        map.put("entityName", tableInfo.getUpClassName());
        mFreemarker.tempWriter(templatePath, "mapper_xml.ftl", packageName, fileName + ".xml", map);
    }

    private void connect() throws Exception {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, NAME, PASS);
        statement = connection.createStatement();
        tableSchema = URL.split("\\?")[0].substring(URL.split("\\?")[0].lastIndexOf("/") + 1);
    }


    /**
     * 解析表信息
     *
     * @param tableName
     * @return
     */
    private TableInfo tableInfo(String tableName) throws Exception {

        LinkedList<ColumnInfo> columnInfoList = new LinkedList();
        LinkedList<ColumnInfo> allColumnInfoList = new LinkedList();
        PreparedStatement columnPs = connection.prepareStatement(SHOW_COLUMN_INFO.replace(REPLACE_TABLE_NAME, tableName));
        ResultSet columnRs = columnPs.executeQuery();
        String primaryKey = "";
        while (columnRs.next()) {
            String tabColumnName = columnRs.getString(1);
            String columnComment = columnRs.getString(2);
            String tabColumnType = columnRs.getString(3);
            //小驼峰
            String lowerColumnName = StrUtils.lowerStr(tabColumnName);
            //大驼峰
            String upColumnName = StrUtils.upStr(lowerColumnName);

            String key = columnRs.getString(4);
            if (null != key && "PRI".equals(key)) {
                primaryKey = tabColumnName;
            }

            ColumnInfo columnInfo = new ColumnInfo(tabColumnName, tabColumnType, StrUtils.sqlTypeToJavaType(tabColumnType), columnComment, lowerColumnName, upColumnName);
            if (enableBaseEntity) {
                if (!Arrays.asList(baseEntityClons).contains(tabColumnName)) {
                    columnInfoList.add(columnInfo);
                } else {
                    if (!baseColumnInfoList.contains(columnInfo))
                        baseColumnInfoList.add(columnInfo);
                }
            } else {
                columnInfoList.add(columnInfo);
            }
            allColumnInfoList.add(columnInfo);
        }

        String lowerClassName = normTableName(tableName);
        String upClassName = StrUtils.upStr(lowerClassName);
        //获取该表注释
        String comment = getTableComment(tableName);
        TableInfo tableInfo = new TableInfo(primaryKey, tableName, lowerClassName, upClassName, comment, columnInfoList, allColumnInfoList);
        return tableInfo;
    }

    /**
     * 规范表名（t_user_info -> UserInfo）
     *
     * @param tableName
     * @return
     */
    private static String normTableName(String tableName) {
        if (TAB_PRE != null && TAB_PRE.length > 0) {
            for (String pre : TAB_PRE) {
                if (tableName.startsWith(pre.toLowerCase()) && !pre.toLowerCase().equals(tableName)) {
                    tableName = tableName.substring(pre.length());
                    break;
                }
            }
        }
        return StrUtils.lowerStr(tableName);
    }

    /**
     * 表注释
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    private String getTableComment(String tableName) throws Exception {
        String str = "";
        String sql = "SELECT table_comment FROM information_schema.tables WHERE table_schema='" + tableSchema + "' AND table_name = '" + tableName + "'";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            str = rs.getString("table_comment");
            if (str == null) {
                str = "";
            } else {
                str = str.replace("\r\n", " ").replace("\n", " ").replace("\r", " ").trim();
            }
        }
        return str;
    }


}
