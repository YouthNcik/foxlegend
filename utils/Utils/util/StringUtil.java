package com.zhph.util;

import java.util.List;

public final class StringUtil {
	private StringUtil() {
	}

	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str));
	}

	public static String isNull(String str){
		return isEmpty(str)?"":str;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static Long toLong(String str) {
		if (str == null)
			return null;
		else
			return Long.valueOf(str);
	}

	public static Long[] toLongArray(String[] strs) {
		if (strs == null)
			return null;
		int len = strs.length;
		Long[] arr = new Long[len];
		for (int i = 0; i < len; i++) {
			arr[i] = toLong(strs[i]);
		}
		return arr;
	}

	public static Long[] splitToLongArray(String str, String regex) {
		if (str == null)
			return null;
		return toLongArray(str.split(regex));
	}

	public static String join(List<String> array, String sep) {
		StringBuffer sb = new StringBuffer();
		if(array != null && array.size() > 0){
			for (String str : array) {
				sb.append(str).append(sep);
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

}
