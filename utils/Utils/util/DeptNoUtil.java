package com.zhph.util;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
public class DeptNoUtil {
//    public  static String getDeptNo(String pid,String deptNo,List<JSONObject> list){
//        String deptNoOut = "";
//        for(JSONObject object:list){
//            if(pid.equals(object.getString("PID"))){
//                if(deptNo.equals(object.getString("DEPTCODE"))){
//                    return object.getString("ID");
//                }
//
//                else
//                    getDeptNo(object.getString("ID"),deptNo,list);
//            }
//        }
//        return deptNoOut;
//    }
    public static String getDeptNo(String pid,String deptNo,List<JSONObject> list){
        String deptNoOut = "";
        for(JSONObject object:list){
            if(pid.equals(object.getString("PID"))){
                if(deptNo.equals(object.getString("DEPTCODE"))){
                    return object.getString("ID");
                }
            }
        }
        return deptNoOut;
    }

}
