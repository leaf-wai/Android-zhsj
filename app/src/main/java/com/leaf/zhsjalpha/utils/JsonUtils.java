package com.leaf.zhsjalpha.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.leaf.zhsjalpha.bean.GradeBean;
import com.leaf.zhsjalpha.bean.OrganizationBean;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
}
