package com.code.generation;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColumnInfo {

    private String tabColumnName;
    private String tableColumnType;
    private String lowerColumnName;
    private String upColumnName;
    private String columnType;
    private String columnComment;
    public ColumnInfo(String tabColumnName, String tableColumnType, String columnType,String columnComment,String lowerColumnName, String upColumnName ) {
        this.tabColumnName = tabColumnName;
        this.tableColumnType = StrUtils.mapperType(tableColumnType);
        this.lowerColumnName = lowerColumnName;
        this.upColumnName = upColumnName;
        this.columnType = columnType;
        this.columnComment = columnComment;
    }
}
