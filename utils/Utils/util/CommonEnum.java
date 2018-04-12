package com.zhph.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxin on 2016/11/23.
 */
public enum CommonEnum {
    /**
     * 社保公积金台账状态
     */
    社保台账_撤销(101,1,"撤销"),
    社保台账_审核中(102,1,"审核中"),
    社保台账_拒绝(103,1,"拒绝"),
    社保台账_已审核(104,1,"已审核"),
    离职社保补缴_撤销(201,2,"撤销"),
    离职社保补缴_审核中(202,2,"审核中"),
    离职社保补缴_拒绝(203,2,"拒绝"),
    离职社保补缴_已审核(204,2,"已审核"),
    消金考勤排班_空_可修改(300,3,""),//可修改
    消金考勤排班_班_可修改(301,3,"班"),//可修改
    消金考勤排班_休_可修改(302,3,"休"),//可修改
    消金考勤排班_空_不可修改(303,3,""),//不可修改
    消金考勤排班_班_不可修改(304,3,"班"),//不可修改
    消金考勤排班_休_不可修改(305,3,"休"),//不可修改
    消金员工状态_在职(1,4,"在职"),
    消金员工状态_离职(2,4,"离职"),
    消金员工状态_停薪留职(401,4,"停薪留职"),
    消金员工状态_停薪停职(402,4,"停薪停职"),
    总部部门状态_启用(501,5,"启用"),
    总部部门状态_停用(502,5,"禁用"),
    总部部门状态_删除(503,5,"删除"),
    总部新增员工审批状态_未审批(601,6,"未审批"),
    总部新增员工审批状态_通过(602,6,"通过"),
    总部新增员工审批状态_拒绝(603,6,"拒绝"),
    总部新增员工审批状态_撤销(604,6,"撤销"),
    总部员工状态_在职(701,6,"在职"),
    总部员工状态_离职(702,6,"离职"),
    总部新增员工附件_员工照片(701,7,"员工照片"),
    总部新增员工附件_面试申请表(702,7,"面试申请表"),
    总部新增员工附件_身份证正反面(703,7,"身份证正反面"),
    总部新增员工附件_其他(704,7,"其他"),
    总部新增员工附件_奖惩附件(705,7,"奖惩附件"),
    ;

    private Integer key;
    private Integer teamId;
    private String value;

    private CommonEnum(Integer key,Integer teamId,String value){
        this.key = key;
        this.teamId = teamId;
        this.value = value;
    }

    /**
     * 根据teamId获取teamList
     * @param teamId
     * @return
     */
    public static List getListByTeamId(Integer teamId){
        List list = new ArrayList();
        for (CommonEnum e: CommonEnum.values()){
            if (e.getTeamId()-teamId==0){
                Map map = new HashMap();
                map.put("key",e.key);
                map.put("value",e.value);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public static String getName(Integer key) {
        try {
            for (CommonEnum e : CommonEnum.values()) {
                if (e.getKey() - key==0) {
                    return e.value;
                }
            }
        }catch (Exception e){}
        return null;
    }

    /**
     * 根据name获取key
     * @param name
     * @return
     */
    public static Integer getKeyByName(String name){
        for (CommonEnum e : CommonEnum.values()) {
            if (name!=null && name.equals(e.getValue())) {
                return e.key;
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
