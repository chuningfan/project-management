package com.sxjkwm.pm.business.material.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.business.material.dto.CategoryDto;
import com.sxjkwm.pm.business.material.dto.MaterialDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/6/1 20:47
 */
@Configuration
public class MaterialCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MaterialService materialService;

    public void init() {
        List<CategoryDto> categoryDtoList = categoryService.fetchAll();
        if (CollectionUtils.isNotEmpty(categoryDtoList)) {
            List<MaterialDto> materialDtoList = materialService.fetchAll();
            if (CollectionUtils.isNotEmpty(materialDtoList)) {
                List<MaterialDto> materials = Lists.newArrayList();
                Map<Long, List<MaterialDto>> materialMap = materialDtoList.stream().collect(Collectors.groupingBy(MaterialDto::getParentId));
                for (CategoryDto dto: categoryDtoList) {
                    List<MaterialDto> materialDtos = materialMap.get(dto.getId());
                    if (CollectionUtils.isNotEmpty(materialDtos)) {
                        materials.addAll(materialDtos);
                    } else {
                        MaterialDto materialDto = new MaterialDto();
                        materialDto.setMaterialName(dto.getCategoryName());
                        materialDto.setParentId(dto.getId());
                        materialDto.setId(dto.getId());
                        materials.add(materialDto);
                    }
                }
                ListOperations<String, Object> ops = redisTemplate.opsForList();
                for (MaterialDto dto: materials) {
                    String key = dto.getMaterialName();
                    if (StringUtils.isNotBlank(key)) {
                        ops.leftPush(key, dto);
                    }
                }
            }
        }
    }

    public List<MaterialDto> getByKey(String key) {
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
        List<MaterialDto> voList = Lists.newArrayList();
        keys.forEach(k ->{
            voList.add((MaterialDto) ops.index(k,-1));
        });
        return voList;
    }

}
