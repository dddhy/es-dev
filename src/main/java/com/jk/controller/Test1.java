package com.jk.controller;

import java.util.HashMap;
import java.util.Map;

public class Test1 {

    public static void main(String[] args) {
        String[] arr = new String[]{"1","2","1","3","2","2","哈哈","哈哈1"};
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i<arr.length ; i++){
            if(map.get(arr[i])!=null){
                map.put(arr[i],map.get(arr[i])+1);
            }else {
                map.put(arr[i],1);
            }
        }
        String s = "";
        for (Map.Entry<String,Integer> entry: map.entrySet()){
            String key = entry.getKey();
            Integer value = entry.getValue();
            s+= key + " ";
            if(value>1){
                System.out.println(key + "有"+value+"个");
            }
        }
        System.out.println(s);
    }

}
