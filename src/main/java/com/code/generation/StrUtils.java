package com.code.generation;

public class StrUtils {

    /**
     * 下划线转驼峰
     * @param para
     * @return
     */
    public static String lowerStr(String para) {
        StringBuilder result = new StringBuilder();
        String[] p = para.split("_");
        for (String s : p) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String upStr(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    //数据类型处理
    public static String sqlTypeToJavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("TINYINT")) {
            return "Byte";
        } else if (sqlType.equalsIgnoreCase("SMALLINT")
                || sqlType.equalsIgnoreCase("MEDIUMINT")) {
            return "Short";
        } else if (sqlType.equalsIgnoreCase("INT")
                || sqlType.equalsIgnoreCase("INTEGER")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("BIGINT")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("FLOAT")
                || sqlType.equalsIgnoreCase("DOUBLE")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("DATE")
                || sqlType.equalsIgnoreCase("TIME")
                || sqlType.equalsIgnoreCase("YEAR")
                || sqlType.equalsIgnoreCase("DATETIME")
                || sqlType.equalsIgnoreCase("TIMESTAMP")) {
            return "Date";
        }
        return "String";
    }

    public static String mapperType(String sqlType) {
        if (sqlType.equalsIgnoreCase("int")) {
            return "INTEGER";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "TIMESTAMP";
        }else if (sqlType.equalsIgnoreCase("text")) {
            return "VARCHAR";
        }
        return sqlType;
    }
}
