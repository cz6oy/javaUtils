package com.kayakwise.gray.api.util.dbentiry;

import java.util.List;

public class TableEntity {
	private String tableName;
    private String boName;
    private String primaryKeyName;
    private String primaryIdName;
    private List columnAttributes;
    private List importClassList;
    private String comment;

    public TableEntity() {
        super();
    }

    public String getBoName() {
        return boName;
    }
    public void setBoName(String boName) {
        this.boName = boName;
    }
    public List getColumnAttributes() {
        return columnAttributes;
    }
    public void setColumnAttributes(List columnAttributes) {
        this.columnAttributes = columnAttributes;
    }
    public List getImportClassList() {
        return importClassList;
    }
    public void setImportClassList(List importClassList) {
        this.importClassList = importClassList;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryIdName() {
        return primaryIdName;
    }

    public void setPrimaryIdName(String primaryIdName) {
        this.primaryIdName = primaryIdName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
