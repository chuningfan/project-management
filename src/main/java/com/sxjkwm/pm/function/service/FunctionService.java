package com.sxjkwm.pm.function.service;

import cn.hutool.core.lang.func.Func;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.function.dao.FunctionDao;
import com.sxjkwm.pm.function.entity.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FunctionService implements ApplicationListener<ContextRefreshedEvent> {

    public static final Map<Long, Function> functionMap = Maps.newHashMap();

    private FunctionDao functionDao;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.functionDao = contextRefreshedEvent.getApplicationContext().getBean(FunctionDao.class);
        List<Function> functions = findAll();
        if (CollectionUtils.isNotEmpty(functions)) {
            functionMap.putAll(functions.stream().collect(Collectors.toMap(Function::getId, f -> f)));
        }
    }

    public void refreshFunctionMap() {
        List<Function> functions = functionDao.findAll();
        if (CollectionUtils.isNotEmpty(functions)) {
            functionMap.clear();
            functionMap.putAll(functions.stream().collect(Collectors.toMap(Function::getId, f -> f)));
        }
    }

    public Function saveOrUpdate(Function function) {
        return functionDao.save(function);
    }

    public List<Function> findAll() {
        Function condition = new Function();
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        Example<Function> example = Example.of(condition);
        return functionDao.findAll(example);
    }

    public Boolean deleteById(Long functionId) {
        functionDao.deleteById(functionId);
        return true;
    }

}
