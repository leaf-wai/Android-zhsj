package com.leaf.zhsjalpha.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.leaf.zhsjalpha.entity.SearchHistory;

@Database(entities = {SearchHistory.class}, version = 1, exportSchema = false)
public abstract class SearchHistoryDatabase extends RoomDatabase {
    private static SearchHistoryDatabase INSTANCE;

    public static synchronized SearchHistoryDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SearchHistoryDatabase.class, "search_history_database")
                    .build();
        }
        return INSTANCE;
    }

    public abstract SearchHistoryDao getSearchHistoryDao();
}
