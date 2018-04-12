package com.zhph.util;

import java.io.Serializable;

/**
 * Created by zhouliang on 2017/11/24.
 *
 */
public class Json implements Serializable{
	
	private static final long serialVersionUID = -2295000548798497398L;

	private boolean success = false;

    private String msg = "";

    private Object obj = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }
    public Object getObj(Object objPram) {
        setObj(objPram);
        return this;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }



	@Override
	public String toString() {
		return "Json [success=" + success + ", msg=" + msg + ", obj=" +( obj == null ? obj : obj.toString()) + "]";
	}
    
    
}
