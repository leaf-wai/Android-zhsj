package com.leaf.zhsjalpha.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.leaf.zhsjalpha.bean.GradeBean;
import com.leaf.zhsjalpha.bean.OrganizationBean;
import com.leaf.zhsjalpha.bean.PostTypeBean;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.ScheduleData;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class JsonUtils {

    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static ArrayList<OrganizationBean> parseOrgData(String result) {
        ArrayList<OrganizationBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                OrganizationBean entity = gson.fromJson(data.optJSONObject(i).toString(), OrganizationBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public static ArrayList<GradeBean> parseGradeData(String result) {
        ArrayList<GradeBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                GradeBean entity = gson.fromJson(data.optJSONObject(i).toString(), GradeBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public static ArrayList<CurrencyTypeData> parseCurrencyTypeData(String result) {
        ArrayList<CurrencyTypeData> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CurrencyTypeData entity = gson.fromJson(data.optJSONObject(i).toString(), CurrencyTypeData.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public static ArrayList<PostTypeBean> parsePostTypeData(String result) {
        ArrayList<PostTypeBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                PostTypeBean entity = gson.fromJson(data.optJSONObject(i).toString(), PostTypeBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public static ScheduleData parseScheduleData(String json) {
        ScheduleData entity = null;
        try {
            Gson gson = new Gson();
            entity = gson.fromJson(json, ScheduleData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public static RequestBody getRequestBody(HashMap<String, Object> hashMap) {
        Gson gson = new Gson();
        String str = gson.toJson(hashMap);
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), str);
    }
}
