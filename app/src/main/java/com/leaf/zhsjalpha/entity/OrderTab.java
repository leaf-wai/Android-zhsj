package com.leaf.zhsjalpha.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

public class OrderTab implements CustomTabEntity {
    public String title;

    public OrderTab(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
