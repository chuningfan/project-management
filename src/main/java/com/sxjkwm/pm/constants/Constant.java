package com.sxjkwm.pm.constants;

public interface Constant<K, V> {

    V getValue();

    K getLabel();

    enum YesOrNo implements Constant<String, Integer> {
        YES(1,"是"),
        NO(0, "否")
        ;

        private Integer value;

        private String label;

        YesOrNo(Integer value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    enum ProjectType implements Constant<String, Integer> {

        INQUIRY(0, "询价项目"), BIDDING(1, "招标项目"),

        ;

        private Integer value;

        private String label;

        ProjectType(Integer value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    enum FileType implements Constant<String, String> {
        INQUIRY("询价文件", "inquiry"), ISSUE_INQUIRY("议题征询单", "issueinquiry"),
        PURCHASE_CONTRACT("采购合同", "purchasecontract"), SALES_CONTRACT("销售合同", "salescontract")

        ;

        private String type;

        private String value;

        FileType(String type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getLabel() {
            return type;
        }
    }

}
