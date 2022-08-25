package com.sxjkwm.pm.util;

public class RMBChange {
    private static String[] nums = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 单位还可以往上
    private static String[] unit = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万"};
    // 小数位如还有需求可以直接往这个数组里加，
    private static String[] countNum = {"角", "分"};
 
    public static String change(String money) {
        // 过滤空
        if (money == null || "".equals(money)) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        String[] splitStr = money.split("\\.");
        if (splitStr.length > 2) {
            throw new RuntimeException("输入的参数不是数字！");
        }
        String front = splitStr[0];
        // 用于判定0的显示
        boolean isZero = true;
        if (front.length() > unit.length) {
            throw new RuntimeException("输入的参数大于万亿！");
        }
        for (int i = 0; i < front.length(); i++) {
            // 整数位处理
            int dw = front.length() - i - 1;
            // 用ASCII码获得数字
            int index = (front.charAt(i) - '0');
            if (index == 0) {
                isZero = true;
                if (dw == 0 || dw == 4  || dw == 8) {
                    // 元，万，亿需要拼接
                    res.append(unit[dw]);
                }
            } else {
                if (isZero && i != 0) {
                    // 多个0只显示一个
                    res.append(nums[0]);
                }
                isZero = false;
                res.append(nums[index]);
                res.append(unit[dw]);
            }
        }
        // 判断是否有小数位
        if (splitStr.length > 1) {
            // 小数位处理，如果不需要则直接删除这个if及内部所有内容
            isZero = true;
            String back = splitStr[1];
            if (back.length() > countNum.length) {
                throw new RuntimeException("小数位小于【" + countNum[countNum.length - 1] + "】！");
            }
            for (int i = 0; i < back.length(); i++) {
                int index = (back.charAt(i) - '0');
                if (index == 0) {
                    isZero = true;
                } else {
                    if (isZero && i != 0) {
                        res.append(nums[0]);
                    }
                    isZero = false;
                    res.append(nums[index]);
                    res.append(countNum[i]);
                }
            }
        }
        return res.toString();
    }

}