package com.esc.laba3_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Provider {
    public String id;
    public String title;
    public String code;
    public String country;
    public String phone;

    public static ArrayList<String> headers = new ArrayList<String>(Arrays.asList("Id", "Title", "Code", "Country", "Phone"));

    public Provider(String id, String title, String code, String country, String phone) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.country = country;
        this.phone = phone;
    }

    public Provider(String title, String code, String country, String phone) {
        this.id = "-";
        this.title = title;
        this.code = code;
        this.country = country;
        this.phone = phone;
    }

    public Provider(Map<String, Object> data){
        this.id = (String) data.get("id");
        this.title = (String) data.get("title");
        this.code = (String) data.get("code");
        this.country = (String) data.get("country");
        this.phone = (String) data.get("phone");
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("code", code);
        data.put("country", country);
        data.put("phone", phone);
        return data;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> data = new ArrayList<String>();
        data.add(id);
        data.add(title);
        data.add(code);
        data.add(country);
        data.add(phone);
        return data;
    }
}
