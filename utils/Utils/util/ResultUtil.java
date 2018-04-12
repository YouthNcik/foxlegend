package com.zhph.util;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.vo.ResultVo;

/**
 * Create By lishuangjiang
 */
public class ResultUtil {

    // JsonObj success 默认为false

    /* success result */
    public static Json success(Object obj){
        Json json = new Json();
        json.setSuccess(true);
        json.setObj(obj);
        return json;
    }

    /* success result */
    public static Json success(){
        return success(null);
    }

    /* failed reslut */
    public static Json error(String message){
        Json json = new Json();
        json.setMsg(message);
        return json;
    }

    /* ResultVo success result*/
    public static ResultVo successVo(String message,String type,Object obj){
        ResultVo vo = new ResultVo();
        vo.setInfo(message);
        vo.setStatus(1);
        vo.setType(type);
        vo.setData(obj);
        return vo;
    }
    public static ResultVo successVo(String message,String type){
        return successVo(message,type,null);
    }

    public static ResultVo successVo(String message){
        return successVo(message,null,null);
    }

    public static ResultVo successVo(){
        return successVo(null,null,null);
    }

    /* ResultVo errorVo result*/
    public static ResultVo errorVo(String message,String type,Object obj){
        ResultVo vo = new ResultVo();
        vo.setInfo(message);
        vo.setStatus(0);
        vo.setType(type);
        vo.setData(obj);
        return vo;
    }

    public static ResultVo errorVo(String message,String type){
        return errorVo(message,type,null);
    }

    public static ResultVo errorVo(String message){
        return errorVo(message,null,null);
    }

    public static ResultVo errorVo(){
        return errorVo(null,null,null);
    }

    /* ResultJsonObj successObj result*/
    public static JSONObject jsonObj(String code, String message, Object obj){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",message);
        json.put("obj",obj);
        return json;
    }

    public static JSONObject jsonObj(String code, String message){
        return jsonObj(code,message,null);
    }

    public static JSONObject jsonObj(String code){
        return jsonObj(code,null,null);
    }


}
