package com.leaf.zhsjalpha.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.leaf.zhsjalpha.entity.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {
    @Insert
    void insertKeyword(SearchHistory searchHistory);

    @Delete
    void deleteKeyword(SearchHistory searchHistory);

    @Query("DELETE FROM SearchHistory WHERE userName = :userName")
    void deleteAllKeyword(String userName);

    @Query("SELECT * FROM SearchHistory WHERE userName = :userName ORDER BY id DESC")
    LiveData<List<SearchHistory>> getAllKeyword(String userName);
}
