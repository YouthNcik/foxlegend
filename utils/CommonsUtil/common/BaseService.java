package com.zhph.service.common;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.common.OperateType;
import com.zhph.model.hqclcf.dto.HqclcfEmpApvQueryParam;

/**
 * Created by zhouliang on 2017/11/24.
 */
public interface BaseService {
	/**
	 * 针对数据字典，将查询的对象序列化为json字符串. 传递到前端 为json格式字符串.
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String getSysDicJsonStr(Object object) throws Exception;

	/**
	 * 封装的 用于将后端数据传送到前端方法（下拉、数据列表）
	 * 
	 * @param modelAndView
	 * @param fdType
	 *            前端展示的类型
	 * @param sysDicKey
	 *            数据字典对应的key
	 * @return
	 * @throws Exception
	 */
	public String addObject(ModelAndView modelAndView, Integer fdType, String sysDicKey) throws Exception;

	/**
	 * 获取当前用户拥有的业务条线
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getOnlineUserBl() throws Exception;

	/**
	 * 构建业务条线下拉框（用户没选择的业务条线就禁用）-通过用户选择的业务条线
	 */
	List<HqclcfEmpApvQueryParam> buildBlByUserAndShowEnable() throws Exception;

	/**
	 * 把对象中的Code转成Name--SYS_CONFIG_TYPE
	 * 
	 * @param data
	 * @param map
	 * @return
	 */
	public JSONObject changeCodeToName(Object data, HashMap<String, String> map);

	/**
	 * SYS_CONFIG_TYPE 单个转化
	 * 
	 * @param dmlx
	 * @param dm
	 * @return
	 */
	public String getCodeNameByDmlxAndDm(String dmlx, String dm);

	/**
	 * 保存日志
	 * 
	 * @param oldData
	 * @param newData
	 * @param clazz
	 * @param operateType
	 * @param logger
	 */
	void saveLog(Object oldData, Object newData, Class<?> clazz, OperateType operateType, Logger logger) throws Exception;

	/**
	 * 查询某人在日志表有没有操作记录
	 * 
	 * @param userName
	 * @return
	 */
	Integer queryOperatePeople(String userName);

	/**
	 * 构建消分-查询栏大区分公司营业部，和职务的展示
	 * 
	 * @param request
	 * @throws Exception
	 */
	void buildCfInitReq(HttpServletRequest request) throws Exception;

	/**
	 * 把数据字典加载到缓存中
	 * 
	 * @throws Exception
	 */
	void RedisSysConfigType() throws Exception;
}
