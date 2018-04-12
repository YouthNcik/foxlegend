package com.zhph.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhouliang on 2017/11/30.
 */
public class RandomUtil {
    private RandomUtil() {
    }

    /**
     * 产生UUID
     */
    public static final String generateGUID()
    {
        UUID uuid=UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }

    /**
     * 生成指定位UUID
     */
    public static final String generateGUIDByNum(Integer a) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, a);
    }

    /**
     * 生成12位随机数
     * @return
     */
    public static String getOrderIdByUUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {
            //有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 12 代表长度为12
        // d 代表参数为正数型
        return Md5Util.encode(String.format("%012d", hashCodeV)).substring(0, 12);
    }
}
