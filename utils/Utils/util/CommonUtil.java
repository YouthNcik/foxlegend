package com.zhph.util;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.model.sys.SysUser;
import com.zhph.web.WebContext;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public final class CommonUtil {
	/*
	 * private CommonUtil() { }
	 */

	/**
	 * 加密密码明文
	 * 
	 * @Title: encodePwd @param @param pwd @param @return @return String @throws
	 */
	public static String encodePwd(String pwd) {
		if (pwd == null)
			return null;
		return Md5Util.encode(pwd, true).substring(10, 26);
	}

	/**
	 * 获取自定义主键（根据UUID生成）
	 * 
	 * @Title: getCustomPrimaryKey @param @return @return String @throws
	 */
	public static String getCustomPrimaryKey() {
		String uuid = UUID.randomUUID().toString();
		String uuidString = uuid.replaceAll("-", "");
		return uuidString;
	}

	public static SysUser getOnlineUser(HttpServletRequest req) {
		if (req == null)
			return null;
		HttpSession session = req.getSession();
		if (session == null)
			return null;
		SysUser user = (SysUser) session.getAttribute("onlineUser");
		return user;
	}

	public static SysUser getOnlineUser() {
		HttpServletRequest req = WebContext.getRequest();
		return getOnlineUser(req);
	}

	public static String getOnlineUserId(HttpServletRequest req) {
		SysUser user = getOnlineUser(req);
		if (user == null)
			return null;
		return user.getUserId();
	}

	public static String getOnlineUserId() {
		HttpServletRequest req = WebContext.getRequest();
		return getOnlineUserId(req);
	}

	public static String getOnlineUserName(HttpServletRequest req) {
		SysUser user = getOnlineUser(req);
		if (user == null)
			return null;
		return user.getUserName();
	}

	public static String getOnlineUserName() {
		HttpServletRequest req = WebContext.getRequest();
		return getOnlineUserName(req);
	}

	public static String getOnlineFullName(HttpServletRequest req) {
		SysUser user = getOnlineUser(req);
		if (user == null)
			return null;
		return user.getFullName();
	}

	public static String getOnlineFullName() {
		HttpServletRequest req = WebContext.getRequest();
		return getOnlineUserName(req);
	}

	/**
	 * 判断浏览器用户是否登陆
	 */
	public static boolean isAccessable(HttpServletRequest req) {
		HttpSession session = req.getSession();
		SysUser user = (SysUser) session.getAttribute("onlineUser");
		if (user == null)
			return false;
		else
			return true;
	}

	public static boolean isAccessable() {
		HttpServletRequest req = WebContext.getRequest();
		HttpSession session = req.getSession();
		SysUser user = (SysUser) session.getAttribute("onlineUser");
		if (user == null)
			return false;
		else
			return true;
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HelloWorld->HELLO_WORLD
	 * 
	 * @param name
	 *            转换前的驼峰式命名的字符串
	 * @return 转换后下划线大写方式命名的字符串
	 */
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			// 将第一个字符处理成大写
			result.append(name.substring(0, 1).toUpperCase());
			// 循环处理其余字符
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toUpperCase());
			}
		}
		return result.toString();
	}

	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HELLO_WORLD->HelloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母小写
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String camels[] = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	/**
	 * 字符串控制判断
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean StringIfNullOrEmpty(String str) {
		if ("".equals(str) || "null".equals(str) || str == null || "undefined".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串空值转为''
	 * 
	 * @param str
	 * @return
	 */
	public static String ChangeNullString(String str) {
		if ("".equals(str) || "null".equals(str) || str == null || "undefined".equals(str)) {
			return "";
		} else {
			return str;
		}
	}

	/**
	 * OBJ 对象转Map 主要用于http请求传参
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> objectToMap(Object obj) throws Exception {
		if(obj == null){
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(obj);//Object对象转json字符串
		Map map = mapper.readValue(jsonStr, Map.class);//json字符串转Map对象
		return map;
	}


	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 *
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim().toLowerCase();
	}
	/**
	 * 获取汉字串拼音，英文字符不变
	 *
	 * @param chinese 汉字串
	 * @return 汉语拼音
	 */
	public static String cn2Spell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}
}
