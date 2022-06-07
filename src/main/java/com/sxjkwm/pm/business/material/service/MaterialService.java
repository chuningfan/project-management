package com.sxjkwm.pm.business.material.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.material.dao.MaterialDao;
import com.sxjkwm.pm.business.material.dto.MaterialDto;
import com.sxjkwm.pm.business.material.entity.Material;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:33
 */
@Service
public class MaterialService {

    private final MaterialDao materialDao;

    @Autowired
    public MaterialService(MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    public Boolean saveOrUpdate(MaterialDto materialDto) {
        Material material = new Material(materialDto);
        materialDao.save(material);
        return true;
    }

    public List<MaterialDto> fetchByParentId(Long parentId) {
        Material condition = new Material();
        condition.setParentId(parentId);
        List<Material> materials = materialDao.findAll(Example.of(condition));
        List<MaterialDto> dataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(materials)) {
            MaterialDto materialDto;
            for (Material material: materials) {
                materialDto = new MaterialDto();
                materialDto.setMaterialName(material.getMaterialName());
                materialDto.setParentId(material.getParentId());
                dataList.add(materialDto);
            }
        }
        return dataList;
    }

    public List<MaterialDto> fetchAll() {
        List<Material> materials = materialDao.findAll();
        List<MaterialDto> dataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(materials)) {
            MaterialDto materialDto;
            for (Material material: materials) {
                materialDto = new MaterialDto();
                materialDto.setMaterialName(material.getMaterialName());
                materialDto.setParentId(material.getParentId());
                materialDto.setId(material.getId());
                dataList.add(materialDto);
            }
        }
        return dataList;
    }

}
