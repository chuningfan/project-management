package com.sxjkwm.pm.business.eplatform.task.parser;

/**
 * @author Vic.Chu
 * @date 2022/8/15 14:34
 */
public interface ResponseParser<T> {

    T parse(String responseJsonString) throws Exception;

}
