package com.zhph.service.common.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.sys.SysUserMapper;
import com.zhph.model.common.OperateType;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysResources;
import com.zhph.model.sys.SysRoles;
import com.zhph.model.sys.SysUser;
import com.zhph.model.vo.TreeVo;
import com.zhph.service.common.BaseService;
import com.zhph.service.common.LoginService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.service.sys.SysResourcesService;
import com.zhph.service.sys.SysRolesService;
import com.zhph.service.sys.SysUserService;
import com.zhph.util.CommonUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.Json;
import com.zhph.util.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {
	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysResourcesService sysResourcesService;

	@Autowired
	private SysRolesService sysRolesService;

	@Resource
	private HqclcfDeptService hqclcfDeptService;

	@Resource
	private HqclcfEmpMapper hqclcfEmpMapper;

	@Autowired
	private SysUserService sysUserService;

	@Resource
	private SysConfigTypeService sysConfigTypeService;

	@Resource
	private BaseService baseService;

	public static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

	@Override
	public Object doLogin(HttpServletRequest req) throws Exception {
		Json json = new Json();

		long id = 282;
		SysConfigType sysConfigType = sysConfigTypeService.queryObjById(id);
		Integer if_yzm = sysConfigType.getSysValue();

		String userName = req.getParameter("userName");
		String pwd = req.getParameter("pwd");
		String yzm = req.getParameter("yzm");

		String yzm_v = (String) req.getSession().getAttribute("yzm_v");
		try {
			if (StringUtil.isEmpty(userName)) {
				json.setSuccess(false);
				json.setObj("500");
				json.setMsg("登录名不能为空");
				return json;
			}
			if (StringUtil.isEmpty(pwd)) {
				json.setSuccess(false);
				json.setObj("500");
				json.setMsg("密码不能为空");
				return json;
			}
			if (if_yzm != 0) {
				if (StringUtil.isEmpty(yzm)) {
					json.setSuccess(false);
					json.setObj("500");
					json.setMsg("验证码不能为空");
					return json;
				}
				if (!yzm.toLowerCase().equals(yzm_v.toLowerCase())) {
					json.setSuccess(false);
					json.setObj("500");
					json.setMsg("验证码不正确");
					return json;
				}
			}
			SysUser user = sysUserMapper.queryUserWithPwdByUserName(userName);
			if (user == null) {
				json.setSuccess(false);
				json.setObj("500");
				json.setMsg("用户不存在或密码错误");
				return json;
			}
			if (user.getIsDelete() == 1) {
				json.setSuccess(false);
				json.setObj("500");
				json.setMsg("用户不存在或密码错误");
				return json;
			}

			if (user.getIsEnable() != 1) {
				json.setSuccess(false);
				json.setObj("500");
				json.setMsg("用户状态异常，请联系管理员");
				return json;
			}
			if (!CommonUtil.encodePwd(pwd).equals(user.getPwd())) {
				int loginErrorCount = user.getLoginErrorCount();
				if (loginErrorCount >= 4) {
					SysUser params = new SysUser();
					params.setUserName(userName);
					params.setIsEnable(0);
					sysUserService.updateLoginErrorCount(params);

					json.setSuccess(false);
					json.setObj("500");
					json.setMsg("密码错误次数达到5次，帐号已禁用，请联系管理员");
					return json;
				} else {
					SysUser params = new SysUser();
					params.setUserName(userName);
					sysUserService.updateLoginErrorCount(params);

					json.setSuccess(false);
					json.setObj("500");
					json.setMsg("用户不存在或密码错误");
					return json;
				}
			}

			// 重新设置用户登录错误次数
			sysUserMapper.resetLoginErrorCount(user.getId());

			// 检查密码
			Date lastChangePwdTime = user.getLastChangePwdTime();
			Date now = new Date();
			if (lastChangePwdTime == null) {
				if (user.getLastLoginTime() == null) {
					json.setSuccess(true);
					json.setObj("501");
					json.setMsg("首次登录请修改密码");
					return json;
				} else {
					json.setSuccess(true);
					json.setObj("502");
					json.setMsg("您的帐号安全性较低，请修改密码");
					return json;
				}
			} else {
				int bwn = DateUtil.daysBetween(lastChangePwdTime, now);
				if (bwn > 30) {
					json.setSuccess(true);
					json.setObj("503");
					json.setMsg("超过30天未修改密码，请先修改密码");
					return json;
				}
			}

			// 员工信息
			HqclcfEmp hqclcfEmp = hqclcfEmpMapper.queryEmpByEmpNo(user.getEmpNo());
			user.setHqclcfEmp(hqclcfEmp);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getUserId());
			List<HqclcfDept> parentDepts = hqclcfDeptService.queryParentDepts(map);
			if (parentDepts != null && parentDepts.size() > 0) {
				user.setCurrentDept(parentDepts.get(0));
				for (HqclcfDept dept : parentDepts) {
					if (Constant.DEPT_TYPE_LEVEL1.equals(dept.getDeptType())) {// 分部--大区--1
						user.setRegion(dept);
					}
					if (Constant.DEPT_TYPE_LEVEL2.equals(dept.getDeptType())) {// 区域--分公司--2
						user.setFiliale(dept);
					}
					if (Constant.DEPT_TYPE_LEVEL3.equals(dept.getDeptType())) {// 营业部--营业部--3
						user.setSalesOfffice(dept);
					}
					if (Constant.DEPT_TYPE_LEVEL4.equals(dept.getDeptType())) {// 团队--4
						user.setTeam(dept);
					}
				}
			}

			List<HqclcfDept> list = hqclcfDeptService.queryAuthedDeptsByUserId(user.getUserId());
			if (list != null && list.size() > 0) {
				List<HqclcfDept> hqDepts = new ArrayList<>();// 授权的部门列表：总部
				List<HqclcfDept> cfDepts = new ArrayList<>();// 授权的部门列表：消分
				List<HqclcfDept> clDepts = new ArrayList<>();// 授权的部门列表：信贷
				for (HqclcfDept hqclcfDept : list) {
					if (Constant.BUSINESS_LINE_HQ.equals(hqclcfDept.getBusinessLine())) {// 总部
						hqDepts.add(hqclcfDept);
					} else if (Constant.BUSINESS_LINE_CF.equals(hqclcfDept.getBusinessLine())) {// 消分
						cfDepts.add(hqclcfDept);
					} else if (Constant.BUSINESS_LINE_CL.equals(hqclcfDept.getBusinessLine())) {// 信贷
						clDepts.add(hqclcfDept);
					}
				}
				user.setCfDepts(cfDepts);
				user.setClDepts(clDepts);
				user.setHqDepts(hqDepts);
			}

			HttpSession session = req.getSession();
			user.setPwd(null);
			user.setPwdBak(null);
			session.setAttribute("onlineUser", user);
			setResourcesInSession(user, req);

			// 登录成功后记录到日志表
			Integer userCount = baseService.queryOperatePeople(user.getUserName());
			if (userCount == 0) {
				baseService.saveLog(null, user, user.getClass(), OperateType.SAVE, logger);
			}

			json.setObj("200");
			json.setSuccess(true);
			return json;
		} catch (AppException e) {
			json.setSuccess(false);
			json.setObj("404");
			json.setMsg(e.getMessage());
			return json;
		}
	}

	/**
	 * 讲登录用户的权限 放进到session中
	 * 
	 * @Title getResourcesByUserId
	 * @param user
	 * @param req
	 * @return TreeVo 返回类型
	 *
	 */
	private void setResourcesInSession(SysUser user, HttpServletRequest req) {
		// 从查询用户对应的所有角色
		List<SysRoles> list = sysRolesService.selectRolesByUserId(user.getUserId());

		if (list.size() > 0) {

			List<Integer> roleIds = new ArrayList<>(); // 根据用户ID 获取角色
			for (SysRoles sysRoles : list) {
				roleIds.add(sysRoles.getRoleId().intValue());
			}

			List<SysResources> resourcesList = sysResourcesService.getResourcesByRoleIds(roleIds);

			List<Long> resourcesIds = new ArrayList<Long>();
			List<String> resourceUrl = new ArrayList<String>();

			for (SysResources sysResource : resourcesList) {
				resourcesIds.add(sysResource.getResourcesId());
				resourceUrl.add(sysResource.getResourcesUrl());
			}

			TreeVo vo = new TreeVo();

			if (resourcesIds.size() > 0) {
				vo = sysResourcesService.findMenuByResourcesId(resourcesIds);
			}

			List<String> allResourceURI = sysResourcesService.findAllURI();

			req.getSession().setAttribute("resources", vo.getChildren());
			req.getSession().setAttribute("resourcesUrl", resourceUrl);
			req.getSession().setAttribute("allResource", allResourceURI);
		}
	}
}
