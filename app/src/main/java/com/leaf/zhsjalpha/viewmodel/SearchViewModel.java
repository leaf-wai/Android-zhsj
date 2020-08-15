package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.leaf.zhsjalpha.entity.SearchHistory;
import com.leaf.zhsjalpha.model.SearchHistoryRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private SearchHistoryRepository repository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        repository = new SearchHistoryRepository(application, userRead.getString("studentName", "guest"));
    }

    public LiveData<List<SearchHistory>> getAllHistoriesLive() {
        return repository.getAllHistoriesLive();
    }

    public void insertHistory(SearchHistory searchHistory) {
        for (int i = 0; i < getAllHistoriesLive().getValue().size(); i++) {
            if (searchHistory.getKeyword().equals(getAllHistoriesLive().getValue().get(i).getKeyword()) && searchHistory.getUserName().equals(getAllHistoriesLive().getValue().get(i).getUserName())) {
                deleteHistory(getAllHistoriesLive().getValue().get(i));
            }
        }
        repository.insertHistory(searchHistory);
    }

    public void deleteAllHistory() {
        repository.deleteAllHistory();
    }

    public void deleteHistory(SearchHistory searchHistory) {
        repository.deleteHistory(searchHistory);
    }
}
