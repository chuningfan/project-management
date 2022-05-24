package com.sxjkwm.pm.constants;

import com.google.common.base.Splitter;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.file.service.ProjectFileService;
import com.sxjkwm.pm.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

//    enum FileType implements Constant<String, String> {
//        ISSUE("方案", "issue"), REPORT("评审报告", "report"),
//        INQUIRY("询价文件", "inquiry"), ISSUE_INQUIRY("议题征询单", "issueinquiry"),
//        PURCHASE_CONTRACT("采购合同", "purchasecontract"), SALES_CONTRACT("销售合同", "salescontract")
//
//        ;
//
//        private String type;
//
//        private String value;
//
//        FileType(String type, String value) {
//            this.type = type;
//            this.value = value;
//        }
//
//        @Override
//        public String getValue() {
//            return value;
//        }
//
//        @Override
//        public String getLabel() {
//            return type;
//        }
//    }

    enum PropertyType implements Constant<String, String> {
        PRICE("金额", "price", BigDecimal.class) {
            @Override
            public BigDecimal getValue(String val) {
                if (StringUtils.isBlank(val)) {
                    return null;
                }
                return new BigDecimal(val);
            }
        },
        STRING("字符串", "string", String.class) {
            @Override
            public String getValue(String val) {
                if (StringUtils.isBlank(val)) {
                    return null;
                }
                return val;
            }
        },
        FILE("文件", "file", Long.class) {
            @Override
            public String getValue(String val) {
                if (StringUtils.isBlank(val)) {
                    return null;
                }
                Long fileId = Long.valueOf(val);
                ProjectFileDao projectFileDao = ContextUtil.getBean(ProjectFileDao.class);
                ProjectFile projectFile = projectFileDao.getOne(fileId);
                String objName = projectFile.getObjectName();
                ProjectFileService projectFileService = ContextUtil.getBean(ProjectFileService.class);
//                projectFileService.
                return null;
            }
        },
        COLLECTION("集合", "collection", List.class) {
            @Override
            public Object getValue(String val) {
                return null;
            }
        },
        CHECKBOX("复选框", "checkbox", String.class) {
            @Override
            public List<Integer> getValue(String val) {
                if (StringUtils.isBlank(val)) {
                    return null;
                }
                List<String> vals = Splitter.on(",").splitToList(val);
                return vals.stream().map(Integer::valueOf).collect(Collectors.toList());
            }
        },
        RADIO("单选框", "radio", String.class) {
            @Override
            public Integer getValue(String val) {
                if (StringUtils.isBlank(val)) {
                    return null;
                }
                return Integer.valueOf(val);
            }
        },
        ;

        private String type;

        private String value;

        private Class<?> clazz;

        PropertyType(String type, String value, Class<?> clazz) {
            this.type = type;
            this.value = value;
            this.clazz = clazz;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getLabel() {
            return type;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public abstract Object getValue(String val);
    }

    enum ProjectNodeStatus implements Constant<String, Integer> {
        INPROGRESS("处理中", 0),
        FINISHED("已完成", 1),
        ;

        private String status;

        private Integer value;

        ProjectNodeStatus(String status, Integer value) {
            this.status = status;
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getLabel() {
            return status;
        }
    }

}
