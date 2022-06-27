package com.sxjkwm.pm.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/6/22 9:30
 */
public class XssFilterRequestWrapper extends HttpServletRequestWrapper {

    public XssFilterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String param = super.getParameter(name);
        return xssEncode(param);
    }

    @Override
    public String[] getParameterValues(String name){
        String[] values = super.getParameterValues(name);
        if(Objects.isNull(values) || values.length == 0){
            return null;
        }
        int count=values.length;
        String[] encodeValues = new String[count];
        for(int i = 0;i < count;i ++){
            encodeValues[i] = xssEncode(values[i]);
        }
        return encodeValues;
    }

    public static String xssEncode(String str){
        if(str == null || str.isEmpty()){
            return str;
        }
        str = str.replaceAll(";", "&#59;");
        str = str.replaceAll("<", "&#60;").replaceAll(">", "&#62;");
        str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        str = str.replaceAll("'", "&#39;").replaceAll("\"", "&#34;");
        str = str.replaceAll("\\$", "&#36;");
        str = str.replaceAll("%", "&#37;");
        str = str.replaceAll("\\/", "&#47;").replaceAll("\\\\", "&#92;");
        str = str.replaceAll(":", "&#58;");
        str = str.replaceAll("\\?", "&#63;").replaceAll("@", "&#64;");
        str = str.replaceAll("\\^", "&#94;");
        return str;
    }

}
