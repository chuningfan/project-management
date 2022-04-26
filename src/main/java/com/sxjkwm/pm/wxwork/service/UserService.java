package com.sxjkwm.pm.wxwork.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.UserDao;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final String userListUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=%s&department_id=%d";

    private final UserDao userDao;

    private final DepartmentService departmentService;

    @Autowired
    public UserService(UserDao userDao, DepartmentService departmentService) {
        this.userDao = userDao;
        this.departmentService = departmentService;
    }

    @Transactional
    @Async
    public Boolean syncUsers(List<Long> wxDeptIds) throws PmException {
        userDao.deleteAll();
        if (CollectionUtils.isEmpty(wxDeptIds)) {
            wxDeptIds = Lists.newArrayList();
            JSONArray deptArray = departmentService.pullDeptFromWxWork();
            if (Objects.nonNull(deptArray) && !deptArray.isEmpty()) {
                Object obj;
                JSONObject deptJson;
                for (int i = 0; i < deptArray.size(); i ++) {
                    deptJson = deptArray.getJSONObject(i);
                    Long deptId = deptJson.getLong("id");
                    wxDeptIds.add(deptId);
                }
            }
        }
        List<User> users = Lists.newArrayList();
        for (Long wxDeptId: wxDeptIds) {
            String url = String.format(userListUrl, WxWorkTokenUtil.getToken(), wxDeptId);
            String res = HttpUtil.get(url);
            JSONObject jsonObject = JSONObject.parseObject(res);
            if (Objects.nonNull(jsonObject) && jsonObject.getInteger("errcode").intValue() == 0) {
                JSONArray array = jsonObject.getJSONArray("userlist");
                if (array.size() == 0) {
                    continue;
                }
                JSONObject userJson;
                User user;
                for (int i = 0; i < array.size(); i ++) {
                    user = new User();
                    userJson = array.getJSONObject(i);
                    user.setWxUserId(userJson.getString("userid"));
                    user.setOpenUserId(userJson.getString("open_userid"));
                    user.setName(userJson.getString("name"));
                    user.setAvatar(userJson.getString("avatar"));
                    JSONArray deptIdArray = userJson.getJSONArray("department");
                    user.setDeptIds(Joiner.on(",").join(deptIdArray));
                    user.setMobile(userJson.getString("mobile"));
                    users.add(user);
                }
            } else {
                throw new PmException(PmError.WXWORK_READ_USER_FAILED, jsonObject.getString("errmsg"));
            }
        }
        if (CollectionUtils.isNotEmpty(users)) {
            userDao.saveAll(users);
        }
        return true;
    }

    public Page<User> findUsersByPage(int pageSize, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return userDao.findAll(pageable);
    }

}
