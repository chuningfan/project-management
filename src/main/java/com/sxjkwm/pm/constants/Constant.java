package com.sxjkwm.pm.constants;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auditing.handler.AuditingDataHandler;
import com.sxjkwm.pm.auditing.handler.impl.ProjectAuditingDataHandler;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.impl.SingleInquiryFileHandler;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public interface Constant<K, V> {

    String openAPISignCachePrefix = "opas:";

    String userCachePrefix = "user:";

    String OPEN_API_FEATURE = "/api/";

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

        INQUIRY(0, "单一来源") {
            @Override
            public String getObjectName() {
                return "inquiry-single-source.docx";
            }

            @Override
            public Map<String, String> getDataMap(ProjectDto projectDto) {
                Map<String, String> dataMap = Maps.newHashMap();
//                dataMap
                return null;
            }
        }, BIDDING(1, "全网询价") {
            @Override
            public String getObjectName() {
                return null;
            }

            @Override
            public Map<String, String> getDataMap(ProjectDto projectDto) {
                return null;
            }
        },
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

        public abstract String getObjectName();

        public abstract Map<String, String> getDataMap(ProjectDto projectDto);

        public static ProjectType getFromVal(Integer val) {
            for (ProjectType projectType: ProjectType.values()) {
                if (projectType.getValue().equals(val)) {
                    return projectType;
                }
            }
            return null;
        }

    }

    enum FileType implements Constant<String, Integer> {
        SINGLE_INQUIRY(1, "询价文件", "单一来源询价文件", 1) {
            @Override
            public Class<? extends PatternFileHandler> fileHandlerClass() {
                return SingleInquiryFileHandler.class;
            }
        },
        NORMAL_INQUIRY(1, "询价文件", "全网询价文件", 2) {
            @Override
            public Class<? extends PatternFileHandler> fileHandlerClass() {
                return SingleInquiryFileHandler.class;
            }
        },
        SPECIF_INQUIRY(1, "询价文件", "指定供应商询价文件", 3) {
            @Override
            public Class<? extends PatternFileHandler> fileHandlerClass() {
                return SingleInquiryFileHandler.class;
            }
        },
        NORMAL_BUY_CONTRACT(2, "合同", "通用采购合同", 1) {
            @Override
            public Class<? extends PatternFileHandler> fileHandlerClass() {
                return SingleInquiryFileHandler.class;
            }
        },
        NORMAL_SALE_CONTRACT(2, "合同", "通用销售合同", 2) {
            @Override
            public Class<? extends PatternFileHandler> fileHandlerClass() {
                return SingleInquiryFileHandler.class;
            }
        },
        ;

        private Integer category;

        private String categoryName;

        private String type;

        private Integer value;

        FileType(Integer category, String categoryName, String type, Integer value) {
            this.category = category;
            this.categoryName = categoryName;
            this.type = type;
            this.value = value;
        }

        public Integer getCategory() {
            return category;
        }

        public String getCategoryName() {
            return categoryName;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getLabel() {
            return type;
        }

        public static FileType getFromVal(Integer targetcategory, Integer value) {
            return Lists.newArrayList(Arrays.asList(FileType.values())).stream().filter(f -> f.getCategory().intValue() == targetcategory.intValue() && f.getValue().intValue() == value.intValue()).findFirst().orElse(null);
        }

        public abstract Class<? extends PatternFileHandler> fileHandlerClass();

    }

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
            public Long getValue(String val) {
                return Objects.nonNull(val) ? Long.valueOf(val) : null;
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
        TEXT("文本域", "text", String.class) {
            @Override
            public Object getValue(String val) {
                return val;
            }
        },
        TIME("时间", "time", Long.class) {
            @Override
            public Object getValue(String val) {
                return Long.valueOf(val);
            }
        },
        MATERIAL("物料", "material", Long.class) {
            @Override
            public Object getValue(String val) {
                return Long.valueOf(val);
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

    enum DataType implements Constant<String, Integer> {
        TRADITIONAL(1, "集采项目") {
            @Override
            public AuditingDataHandler dataHandler() {
                return ContextUtil.getBean(ProjectAuditingDataHandler.class);
            }
        },
        E_PLATFORM(2, "电商项目") {
            @Override
            public AuditingDataHandler dataHandler() {
                return null;
            }
        },
        ;

        private Integer type;

        private String lable;

        DataType(Integer type, String lable) {
            this.type = type;
            this.lable = lable;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        @Override
        public Integer getValue() {
            return type;
        }

        @Override
        public String getLabel() {
            return lable;
        }

        public abstract AuditingDataHandler dataHandler();
    }

}
