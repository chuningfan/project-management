package com.sxjkwm.pm.thirdparty.wxwork.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.DepartmentDao;
import com.sxjkwm.pm.auth.entity.Department;
import com.sxjkwm.pm.configuration.WxConfig;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class DepartmentService {

    private static final String deptListUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=%s";

    private final DepartmentDao departmentDao;

    private final WxConfig wxConfig;

    @Autowired
    public DepartmentService(DepartmentDao departmentDao, WxConfig wxConfig) {
        this.departmentDao = departmentDao;
        this.wxConfig = wxConfig;
    }

    public JSONArray pullDeptFromWxWork() throws PmException {
        String url = String.format(deptListUrl, WxWorkTokenUtil.getToken());
        String res = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (Objects.nonNull(jsonObject) && jsonObject.getInteger("errcode").intValue() == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("department");
            return jsonArray;
        }
        throw new PmException(PmError.WXWORK_READ_DEPARTMENT_LIST_FAILED, jsonObject.getString("errmsg"));
    }

    @Async
    @Transactional
    public Boolean syncDepartments() throws PmException {
        departmentDao.deleteAll();
        JSONArray jsonArray = pullDeptFromWxWork();
        if (Objects.nonNull(jsonArray) && !jsonArray.isEmpty()) {
            List<Department> departmentList = Lists.newArrayList();
            Iterator<Object> itr = jsonArray.iterator();
            Department department;
            Object obj;
            JSONObject deptJson;
            while (itr.hasNext()) {
                obj = itr.next();
                deptJson = JSONObject.parseObject(JSONObject.toJSONString(obj));
                department = new Department();
                department.setWxDeptId(deptJson.getLong("id"));
                department.setDeptName(deptJson.getString("name"));
                department.setParentId(deptJson.getLong("parentid"));
                departmentList.add(department);
            }
            departmentDao.saveAll(departmentList);
        }
        return true;
    }

}
