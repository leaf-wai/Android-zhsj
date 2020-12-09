package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.Resource;

import java.util.List;

public class ResourceViewModel extends AndroidViewModel {

    private MutableLiveData<List<Resource>> resource = new MutableLiveData<>();

    private List<Resource> resourceList;

    public ResourceViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

}
