package com.sxjkwm.pm.business.eplatform.task.constant;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:31
 */
public interface Constants {

    enum PriceTerminationWay {
        FIRST_VAL(1, "取第一次价格"), SECOND_VAL(2, "取第二次价格"), AVG_VAL(3, "取平均价格"), ;

        private Integer val;

        private String label;

        PriceTerminationWay(Integer val, String label) {
            this.val = val;
            this.label = label;
        }

        public Integer getVal() {
            return val;
        }

        public String getLabel() {
            return label;
        }
    }

}
