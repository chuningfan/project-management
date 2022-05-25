package com.sxjkwm.pm.business.org.service;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.org.dao.OrganizationDao;
import com.sxjkwm.pm.business.org.dto.OrgDto;
import com.sxjkwm.pm.business.org.entity.Organization;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/24 18:23
 */
@Service
public class OrganizationService {

    public static final String MAP_KEY_ORG = "sxjkorg";

    private final OrganizationDao organizationDao;

    private final RedissonClient redissonClient;

    private final RMap<String, String> ortMap;

    @Autowired
    public OrganizationService(OrganizationDao organizationDao, @Qualifier("orgRedissonClient") RedissonClient redissonClient) {
        this.organizationDao = organizationDao;
        this.redissonClient = redissonClient;
        ortMap = this.redissonClient.getMap(MAP_KEY_ORG);
    }

    public List<Organization> getAllOrgs() {
        List<Organization> organizationList = organizationDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return organizationList;
    }

    public void initDataToRedis() {
        List<Organization> organizationList = this.getAllOrgs();
        if (CollectionUtils.isNotEmpty(organizationList)) {
            organizationList.forEach(o -> {
                OrgDto dto = new OrgDto();
                dto.setId(o.getId());
                String key = o.getOrgName();
                dto.setOrgName(key);
                String val = JSONObject.toJSONString(dto);
                ortMap.put(key, val);
            });
        }
    }

    public Collection<String> getByKey(String key) {
        return ortMap.values(key, 100);
    }

}
