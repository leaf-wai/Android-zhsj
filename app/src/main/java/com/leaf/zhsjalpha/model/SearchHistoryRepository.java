package com.leaf.zhsjalpha.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.leaf.zhsjalpha.database.SearchHistoryDao;
import com.leaf.zhsjalpha.database.SearchHistoryDatabase;
import com.leaf.zhsjalpha.entity.SearchHistory;

import java.util.List;

public class SearchHistoryRepository {

    private LiveData<List<SearchHistory>> allHistoriesLive;
    private SearchHistoryDao dao;
    private SearchHistoryDatabase database;
    private String studentName;

    public SearchHistoryRepository(Context context, String userName) {
        database = SearchHistoryDatabase.getDatabase(context.getApplicationContext());
        dao = database.getSearchHistoryDao();
        allHistoriesLive = dao.getAllKeyword(userName);
        studentName = userName;
    }

    public LiveData<List<SearchHistory>> getAllHistoriesLive() {
        return allHistoriesLive;
    }

    public void insertHistory(SearchHistory searchHistory) {
        Thread thread = new Thread(() -> dao.insertKeyword(searchHistory));
        thread.start();
    }

    public void deleteAllHistory() {
        Thread thread = new Thread(() -> dao.deleteAllKeyword(studentName));
        thread.start();
    }

    public void deleteHistory(SearchHistory searchHistory) {
        Thread thread = new Thread(() -> dao.deleteKeyword(searchHistory));
        thread.start();
    }

}
