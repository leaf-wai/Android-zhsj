package com.leaf.zhsjalpha.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SearchHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userName")
    private String userName;
    @ColumnInfo(name = "keyword")
    private String keyword;

    public SearchHistory(String userName, String keyword) {
        this.userName = userName;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
