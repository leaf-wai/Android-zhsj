package com.leaf.zhsjalpha.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.SearchHistory;
import com.leaf.zhsjalpha.model.SearchHistoryRepository;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.viewholder.ChipsViewHolder;

import java.util.List;

public class ChipsAdapter extends RecyclerView.Adapter<ChipsViewHolder> {

    private List<SearchHistory> searchHistories;

    public void setSearchHistories(List<SearchHistory> searchHistories) {
        this.searchHistories = searchHistories;
    }

    @NonNull
    @Override
    public ChipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_search_history_item, parent, false);
        return new ChipsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipsViewHolder holder, int position) {
        SearchHistory searchHistory = searchHistories.get(position);
        holder.chip.setText(searchHistory.getKeyword());
        holder.chip.setOnCloseIconClickListener(v -> {
            removeData(searchHistory, position);
        });
    }

    @Override
    public int getItemCount() {
        if (searchHistories == null) {
            return 0;
        } else {
            return searchHistories.size();
        }
    }

    private void removeData(SearchHistory searchHistory, int position) {
        SharedPreferences userRead = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SearchHistoryRepository repository = new SearchHistoryRepository(MyApplication.getContext(), userRead.getString("studentName", "guest"));
        repository.deleteHistory(searchHistory);
        searchHistories.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

}
