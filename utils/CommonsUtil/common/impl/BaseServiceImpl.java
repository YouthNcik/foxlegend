package com.zhph.service.common.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.mapper.common.BaseMapper;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.model.common.OperateType;
import com.zhph.model.common.RankType;
import com.zhph.model.common.SysLog;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.dto.HqclcfEmpApvQueryParam;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysUser;
import com.zhph.redis.BaseRedisDao;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfBusinessService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

/**
 * Created by zhouliang on 2017/11/24.
 */
@Service
public class BaseServiceImpl implements BaseService {

	@Resource
	private ZhphObjectMapper zhphObjectMapper;

	@Resource
	private SysConfigTypeService sysConfigTypeService;

	@Resource
	private BaseMapper baseMapper;

	@Resource
	private HqclcfDeptService hqclcfDeptService;

	@Resource
	private HqclcfBusinessService hqclcfBusinessService;

	@Autowired
	private BaseRedisDao<String, List<SysConfigType>> baseRedisDao;

	/**
	 * 针对数据字典，将查询的对象序列化为json字符串. 传递到前端 为json格式字符串.
	 *
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getSysDicJsonStr(Object object) throws Exception {
		return zhphObjectMapper.writeValueAsString(object);
	}

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
	@Override
	public String addObject(ModelAndView modelAndView, Integer fdType, String sysDicKey) throws Exception {
		String key = "";
		Json json = new Json();
		List<SysConfigType> sysConfigTypeList = baseRedisDao.get(Constant.SALARY_PROJECT + "_" + sysDicKey);

		switch (fdType) {
		case 1:
			key = CommonUtil.camelName(sysDicKey) + "List";
			modelAndView.addObject(key, sysConfigTypeList);
			break;
		case 2:
			key = CommonUtil.camelName(sysDicKey) + "ListTpl";
			modelAndView.addObject(key, getSysDicJsonStr(json.getObj(sysConfigTypeList)));
			break;
		}
		return key;
	}

	@Override
	public List<Integer> getOnlineUserBl() throws Exception {
		List<Integer> blList = new ArrayList<>();
		SysUser user = CommonUtil.getOnlineUser();
		if (user != null)
			if (user.getBlSelect() != null && !"".equals(user.getBlSelect())) {
				String blStr = user.getBlSelect();
				String[] blStrList = blStr.split(Constant.SPOT);
				for (int i = 0; i < blStrList.length; i++) {
					if (Integer.valueOf(blStrList[i]) == 0) {
						blList.add(1);
						blList.add(2);
						blList.add(3);
					} else {
						blList.add((2 == Integer.valueOf(blStrList[i])) ? 3 : (3 == Integer.valueOf(blStrList[i])) ? 2 : 1);
					}
				}
			}
		return blList;
	}

	@Override
	public List<HqclcfEmpApvQueryParam> buildBlByUserAndShowEnable() throws Exception {
		List<HqclcfEmpApvQueryParam> params = new ArrayList<>();
		Map<Integer, Integer> blMap = new HashMap<>();
		for (Integer bl : getOnlineUserBl()) {
			blMap.put(bl, bl);
		}
		for (SysConfigType type : sysConfigTypeService.getConfigTypesByPSysCode(Constant.BUSINESS_LINE)) {
			HqclcfEmpApvQueryParam param = new HqclcfEmpApvQueryParam();
			if (null != blMap.get(type.getSysValue()) ? true : false) {
				param.setFlag(true);
			} else {
				param.setFlag(false);
			}
			param.setSysConfigType(type);
			params.add(param);
		}
		return params;
	}

	@Override
	public JSONObject changeCodeToName(Object data, HashMap<String, String> map) {
		// 将对象转化为json格式对象
		Object obj = JSONObject.toJSON(data);
		// 转化为json对象
		JSONObject json1 = JSONObject.parseObject(obj.toString());

		JSONObject json2 = new JSONObject();
		Set<String> s = json1.keySet();// 获取所有的键
		for (String string : s) {
			if (map.containsKey(string)) {
				String code_lx = map.get(string).toString();
				String code = json1.get(string).toString();
				String code_name = sysConfigTypeService.getCofigNameByCodeAndPSysCode(code_lx, Integer.valueOf(code));
				json2.put(string + "_name", code_name);
			}
			json2.put(string, json1.get(string));
		}

		return json2;
	}

	@Override
	public String getCodeNameByDmlxAndDm(String dmlx, String dm) {
		if (!"".equals(dm) && !"null".equals(dm) && dm != null) {
			return sysConfigTypeService.getCofigNameByCodeAndPSysCode(dmlx, Integer.valueOf(dm));
		} else {
			return "";
		}

	}

	@Override
	public void saveLog(Object oldData, Object newData, Class<?> clazz, OperateType operateType, Logger logger) throws Exception {

		SysLog log = new SysLog();
		if (oldData != null)
			log.setOldData(JSONObject.toJSONString(oldData));
		if (newData != null)
			log.setNewData(JSONObject.toJSONString(newData));
		log.setCreateName(null != CommonUtil.getOnlineFullName() ? CommonUtil.getOnlineFullName() : "");
		log.setCreateTime(new Date());
		log.setOperateType(operateType);
		log.setOperateClass(clazz.getName());

		baseMapper.saveLog(log);
	}

	@Override
	public Integer queryOperatePeople(String userName) {
		Map<String, Object> map = new HashMap<>();
		map.put("createName", userName);
		List<SysLog> sysLogs = baseMapper.queryLogByUserName(map);
		return sysLogs.size();
	}

	@Override
	public void buildCfInitReq(HttpServletRequest request) throws Exception {
		SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
		List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
		if (onlineUser.getHqclcfEmp() != null && RankType.SALES_MANAGER.getNum().equals(onlineUser.getHqclcfEmp().getPosition())) {// 职务是营业部经理
			if (onlineUser.getFiliale() != null) {// 营业部经理可以处理上级为分公司的部门的数据
				cfDepts.add(onlineUser.getFiliale());
			}
		}

		Set<HqclcfDept> dqSet = new HashSet<HqclcfDept>();
		Set<HqclcfDept> orgSet = new HashSet<HqclcfDept>();
		Set<HqclcfDept> saleSet = new HashSet<HqclcfDept>();
		List<Long> deptIds = new ArrayList<Long>();
		if (cfDepts != null && cfDepts.size() > 0) {
			for (HqclcfDept dept : cfDepts) {
				deptIds.add(dept.getId());
			}
		}
		// 获取授权的部门信息及上级部门信息（被授权的部门的上级部门信息需要被展示）
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "up");
		map.put("deptIds", deptIds);
		List<HqclcfDept> allDepts = hqclcfDeptService.queryParentDepts(map);// 授权部门及上级部门信息

		request.setAttribute("depts", getSysDicJsonStr(allDepts));
		if (allDepts != null && allDepts.size() > 0) {
			for (HqclcfDept dept : allDepts) {
				if (Constant.DEPT_TYPE_LEVEL1.equals(dept.getDeptType())) {// 分部--大区--1
					dqSet.add(dept);
				}
				if (Constant.DEPT_TYPE_LEVEL2.equals(dept.getDeptType())) {// 区域--分公司--2
					orgSet.add(dept);
				}
				if (Constant.DEPT_TYPE_LEVEL3.equals(dept.getDeptType())) {// 营业部--营业部--3
					saleSet.add(dept);
				}
			}
		}

		HqclcfBusiness business = new HqclcfBusiness();
		PageBean pb = new PageBean();
		pb.setLimit(10000);
		Grid<HqclcfBusiness> grid = hqclcfBusinessService.getBusinessByCondition(pb, business);// 查询职务列表

		request.setAttribute("rankTypes", grid.getData());
		request.setAttribute("dqList", dqSet);
		request.setAttribute("orgList", orgSet);
		request.setAttribute("salaList", saleSet);
	}

	@Override
	public void RedisSysConfigType() throws Exception {

		Json json = new Json();
		List<SysConfigType> sct_list = sysConfigTypeService.selectAllConfigType();
		if (sct_list.size() > 0) {
			for (SysConfigType sct : sct_list) {
				String p_sys_code = sct.getpSysCode();
				List<SysConfigType> code_list = sysConfigTypeService.getConfigTypesByPSysCode(p_sys_code);
				baseRedisDao.delete(Constant.SALARY_PROJECT + "_" + p_sys_code);
				baseRedisDao.add(Constant.SALARY_PROJECT + "_" + p_sys_code, code_list);
			}
		}
	}
}
