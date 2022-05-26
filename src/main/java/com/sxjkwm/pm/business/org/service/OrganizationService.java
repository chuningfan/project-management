package com.sxjkwm.pm.business.org.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.business.org.dao.OrganizationDao;
import com.sxjkwm.pm.business.org.dto.OrgDto;
import com.sxjkwm.pm.business.org.entity.Organization;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Vic.Chu
 * @date 2022/5/24 18:23
 */
@Service
public class OrganizationService {

    private final OrganizationDao organizationDao;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public OrganizationService(OrganizationDao organizationDao, RedisTemplate<String, Object> redisTemplate) {
        this.organizationDao = organizationDao;
        this.redisTemplate = redisTemplate;
    }

    public List<Organization> getAllOrgs() {
        List<Organization> organizationList = organizationDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return organizationList;
    }

    public void initDataToRedis() {
        List<Organization> organizationList = this.getAllOrgs();
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        if (CollectionUtils.isNotEmpty(organizationList)) {
            organizationList.forEach(o -> {
                OrgDto dto = new OrgDto();
                dto.setId(o.getId());
                String key = o.getOrgName();
                dto.setOrgName(key);
                ops.leftPush(key, dto);
            });
        }
    }

    public List<OrgDto> getByKey(String key) {
        Set<String> keys = Sets.newLinkedHashSet();
        redisTemplate.execute((RedisConnection connection) -> {
            String searchKey = "*";
            if (StringUtils.isNotBlank(key)) {
                searchKey = "*" + key + "*";
            }
            try (Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions()
                            .count(Long.MAX_VALUE)
                            .match(searchKey)
                            .build()
            )) {
                cursor.forEachRemaining(item -> {
                    keys.add((String) redisTemplate.getKeySerializer().deserialize(item));
                });
                return keys;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        ListOperations ops = redisTemplate.opsForList();
        List<OrgDto> voList = Lists.newArrayList();
        keys.forEach(k ->{
            voList.add((OrgDto) ops.index(k,-1));
        });
        return voList;
    }

}
