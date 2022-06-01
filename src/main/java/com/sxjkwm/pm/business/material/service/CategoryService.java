package com.sxjkwm.pm.business.material.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.material.dao.CategoryDao;
import com.sxjkwm.pm.business.material.dto.CategoryDto;
import com.sxjkwm.pm.business.material.entity.Category;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:27
 */
@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Transactional
    public Boolean saveOrUpdate(CategoryDto categoryDto) {
        categoryDao.save(new Category(categoryDto));
        return true;
    }

    public List<CategoryDto> fetchAll() {
        List<Category> categories = categoryDao.findAll();
        List<CategoryDto> dataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(categories)) {
            CategoryDto dto;
            for (Category category: categories) {
                dto = new CategoryDto();
                dto.setCategoryName(category.getCategoryName());
                dto.setId(category.getId());
                dataList.add(dto);
            }
        }
        return dataList;
    }

}
