package com.esc.laba3_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Chemical {
    public String id;
    public String title;
    public String type;
    public Double price;
    public Double weight;
    public String providerCode;
    public Integer count;

    public static ArrayList<String> headers = new ArrayList<String>(Arrays.asList("Id", "Title", "Type", "Provider", "Price", "Weight", "Count"));

    public Chemical(String id, String title, String type, Double price, Double weight, String providerCode, Integer count) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.price = price;
        this.weight = weight;
        this.providerCode = providerCode;
        this.count = count;
    }

    public Chemical(String title, String type, Double price, Double weight, String providerCode, Integer count) {
        this.id = "-";
        this.title = title;
        this.type = type;
        this.price = price;
        this.weight = weight;
        this.providerCode = providerCode;
        this.count = count;
    }

    public Chemical(Map<String, Object> data){
        this.id = (String) data.get("id");
        this.title = (String) data.get("title");
        this.type = (String) data.get("type");
        this.providerCode = (String) data.get("provider");
        this.price = Double.parseDouble(data.get("price").toString());
        this.weight = Double.parseDouble(data.get("weight").toString());
        this.count = Integer.parseInt(data.get("count").toString());
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("type", type);
        data.put("provider", providerCode);
        data.put("price", price);
        data.put("weight", weight);
        data.put("count", count);
        return data;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> data = new ArrayList<String>();
        data.add(id);
        data.add(title);
        data.add(type);
        data.add(providerCode);
        data.add(Double.toString(price));
        data.add(Double.toString(weight));
        data.add(Integer.toString(count));
        return data;
    }

    public String getId() { return id; }
}
