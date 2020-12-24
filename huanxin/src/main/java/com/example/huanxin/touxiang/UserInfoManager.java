package com.example.huanxin.touxiang;

import java.util.ArrayList;
import java.util.List;

//提供和维护数据       想象成数据库
public class UserInfoManager {

    //存储好友的列表
    private List<EMUserInfo> userList = new ArrayList<>();

    private static UserInfoManager infoManager;

    public static UserInfoManager getInstance(){
        if(infoManager == null){
            infoManager = new UserInfoManager();
        }
        return infoManager;
    }

    /**
     * 添加用户信息
     * @param list
     */
    public void addUsers(List<EMUserInfo> list){
        userList.clear();
        userList.addAll(list);
    }

    /**
     * 获取所有的用户信息
     * @return
     */
    public List<EMUserInfo> getAllUsers(){
        return userList;
    }

    /**
     * 通过uid查找用户信息
     * @param uid
     * @return
     */
    public EMUserInfo getUserInfoByUid(String uid ){
        for(EMUserInfo item:userList){
            if(item.getUid().equals(uid)){
                return item;
            }
        }
        return null;
    }
}
