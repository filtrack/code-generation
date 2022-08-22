package com.code.generation;

public class Test {

    private static final String URL = "jdbc:mysql://localhost:3306/myapp?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ZIjing123";

    private static final String[] TABS = new String[]{"t_topic","t_user","t_launage","t_aricle"};
    private static final String[] TAB_PRE = new String[]{"t_"};
    private static final String BASE_PACKAGE = "com.starter.app";
    private static final String[] BASE_ENTITY_CLNS = new String[]{"id","delete_flag","version","create_time","update_time"};


    public static void main(String[] args) throws Exception {

        AutoCode.init().initDB(URL, USERNAME, PASSWORD)
                .tableInfo(TABS,TAB_PRE,BASE_PACKAGE)
                .overWrite(true)
                .genService(true)
                .genMapper(true)
                .genController(true)
                .baseEntityInfo(BASE_ENTITY_CLNS)
                .genCode();

    }
}
