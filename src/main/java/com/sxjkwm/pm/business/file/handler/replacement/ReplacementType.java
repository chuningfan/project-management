package com.sxjkwm.pm.business.file.handler.replacement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/**
 * @author Vic.Chu
 * @date 2022/7/8 14:29
 */
public enum ReplacementType {

    STRING() {
        @Override
        public void convert(XWPFDocument document, BaseReplacement replacement) {
            Object data = replacement.getData();
            handleStringValue(document, replacement.getKey(), Objects.isNull(data) ? null : data.toString(), replacement.getFontFamily(), replacement.getFontSize());
        }
    },
    TABLE() {
        @Override
        public void convert(XWPFDocument document, BaseReplacement replacement) {
            Object data = replacement.getData();
            if (Objects.isNull(data)) {
                return;
            }
            List<List<LinkedHashMap<String, String>>> tables = (List<List<LinkedHashMap<String, String>>>) data;
            handleTableValue(document, replacement.getKey(), tables, null, replacement.getFontFamily(), replacement.getFontSize());
        }
    },
    TABLE_WITH_MERGED_FIRST_COLUMN() {
        @Override
        public void convert(XWPFDocument document, BaseReplacement replacement) {
            Object data = replacement.getData();
            if (Objects.isNull(data)) {
                return;
            }
            LinkedHashMap<String, List<LinkedHashMap<String, String>>> tables = (LinkedHashMap<String, List<LinkedHashMap<String, String>>>) data;
            handleTableWithMergedFirstColumn(document, replacement.getKey(), tables, replacement.getFontFamily(), replacement.getFontSize());
        }
    }, PRICE() {
        @Override
        public void convert(XWPFDocument document, BaseReplacement replacement) {
            Object data = replacement.getData();
            if (Objects.isNull(data)) {
                return;
            }
            String value = null;
            if (data instanceof Number) {
                value = data.toString();
            } else {
                value = (String) data;
            }
            handleStringValue(document, replacement.getKey(), value, replacement.getFontFamily(), replacement.getFontSize());
        }
    }, DATE() {
        @Override
        public void convert(XWPFDocument document, BaseReplacement replacement) {
            Object data = replacement.getData();
            if (Objects.isNull(data)) {
                return;
            }
            String value = null;
            Date date;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日HH时mm分ss秒");
            if ((data instanceof Date)) {
                date = (Date) data;
                value = simpleDateFormat.format(date);
            }
            if ((data instanceof Long) && Objects.isNull(value)) {
                date = Date.from(Instant.ofEpochMilli((Long)data));
                value = simpleDateFormat.format(date);
            }
            if (Objects.isNull(value)) {
                value = data.toString();
            }
            handleStringValue(document, replacement.getKey(), value, replacement.getFontFamily(), replacement.getFontSize());
        }
    }, SIGN() {
        @Override
        public void convert(XWPFDocument document, BaseReplacement replacement) {
            Object data = replacement.getData();
            if (Objects.isNull(data)) {
                return;
            }
            InputStream inputStream = (InputStream) data;
            try {
                handleImgValue(document, replacement.getKey(), inputStream);
            } catch (IOException | InvalidFormatException e) {
                logger.error("Insert image into document failed: {}", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    };
    private static final Logger logger = LoggerFactory.getLogger(ReplacementType.class);
    public abstract void convert(XWPFDocument document, BaseReplacement replacement);

    void handleStringValue(XWPFDocument document, String key, String value, String fontFamily, Integer fontSize) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        String rKey = "{{" + key + "}}";
        for (XWPFParagraph paragraph: paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run: runs) {
                String text = run.getText(0);
                if (StringUtils.isNotBlank(text) && text.indexOf(rKey) != -1) {
                    text = text.replace(rKey, value);
                    if (StringUtils.isNotBlank(fontFamily)) {
                        run.setFontFamily(fontFamily);
                    }
                    if (Objects.nonNull(fontSize)) {
                        run.setFontSize(fontSize);
                    }
                    run.setText(text, 0);
                }
            }
        }
        List<XWPFTable> tables = document.getTables();
        if (CollectionUtils.isNotEmpty(tables)) {
            for (XWPFTable table: tables) {
                List<XWPFTableRow> rows = table.getRows();
                for (XWPFTableRow row: rows) {
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell: cells) {
                        List<XWPFParagraph> cellParagraphs = cell.getParagraphs();
                        for (XWPFParagraph cellParagraph: cellParagraphs) {
                            List<XWPFRun> runs = cellParagraph.getRuns();
                            for (XWPFRun run: runs) {
                                String text = run.getText(0);
                                if (StringUtils.isNotBlank(text) && text.indexOf(rKey) != -1) {
                                    text = text.replace(rKey, value);
                                    if (StringUtils.isNotBlank(fontFamily)) {
                                        run.setFontFamily(fontFamily);
                                    }
                                    if (Objects.nonNull(fontSize)) {
                                        run.setFontSize(fontSize);
                                    }
                                    run.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void handleTableWithMergedFirstColumn(XWPFDocument document, String key, LinkedHashMap<String, List<LinkedHashMap<String, String>>> tables, String fontFamily, Integer fontSize) {
        Set<Map.Entry<String, List<LinkedHashMap<String, String>>>> tableEntries = tables.entrySet();
        Iterator<Map.Entry<String, List<LinkedHashMap<String, String>>>> tableIterator = tableEntries.iterator();
        List<List<LinkedHashMap<String, String>>> tableList = Lists.newArrayList();
        while (tableIterator.hasNext()) {
            Map.Entry<String, List<LinkedHashMap<String, String>>> tableEntry = tableIterator.next();
            String tableKey = tableEntry.getKey();
            List<LinkedHashMap<String, String>> rows = tableEntry.getValue();
            List<LinkedHashMap<String, String>> newRows = Lists.newArrayList();
            for (int i = 0; i < rows.size(); i ++) {
                LinkedHashMap<String, String> rowData = rows.get(i);
                LinkedHashMap<String, String> newRowData = Maps.newLinkedHashMap();
                newRowData.put(tableKey, tableKey);
                newRowData.putAll(rowData);
                newRows.add(newRowData);
            }
            tableList.add(newRows);
        }
        handleTableValue(document, key, tableList, true, fontFamily, fontSize);
    }

    void handleTableValue(XWPFDocument document, String key, List<List<LinkedHashMap<String, String>>> tables, Boolean mergeFirstColumn, String fontFamily, Integer fontSize) {
        if (Objects.isNull(mergeFirstColumn)) {
            mergeFirstColumn = false;
        }
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        String rKey = "{{" + key + "}}";
        for (XWPFParagraph paragraph: paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            Iterator<XWPFRun> iterator = runs.iterator();
            while (iterator.hasNext()) {
                XWPFRun run = iterator.next();
                String text = run.getText(0);
                if (StringUtils.isNotBlank(text) && text.indexOf(rKey) != -1) {
                    List<String> headers;
                    for (List<LinkedHashMap<String, String>> table: tables) {
                        LinkedHashMap<String, String> firstRow = table.get(0);
                        headers = Lists.newArrayList(firstRow.keySet());
                        List<List<String>> rowValues = Lists.newArrayList();
                        for (Map<String, String> rowData: table) {
                            List<String> values = Lists.newArrayList();
                            for (String header : headers) {
                                String val = rowData.getOrDefault(header, "");
                                values.add(val);
                            }
                            rowValues.add(values);
                        }
                        // create table
                        XmlCursor cursor = paragraph.getCTP().newCursor();
                        XWPFTable newTable = document.insertNewTbl(cursor);
                        CTTblPr tblPr = newTable.getCTTbl().getTblPr();
                        tblPr.getTblW().setType(STTblWidth.DXA);
                        tblPr.getTblW().setW(new BigInteger("10000"));
                        // create headers
                        XWPFTableCell firstCell = newTable.getRow(0).getCell(0); // init table
                        if (mergeFirstColumn) {
                            CTVMerge vmerge = CTVMerge.Factory.newInstance();
                            vmerge.setVal(STMerge.RESTART);
                            firstCell.getCTTc().addNewTcPr().setVMerge(vmerge);
                            firstCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                        }
                        XWPFRun firstCellRun = firstCell.addParagraph().createRun();
                        if (StringUtils.isNotBlank(fontFamily)) {
                            firstCellRun.setFontFamily(fontFamily);
                        }
                        if (Objects.nonNull(fontSize)) {
                            firstCellRun.setFontSize(fontSize);
                        }
                        firstCellRun.setText(headers.get(0), 0);
//                        firstCell.setText(headers.get(0));
                        for (int i = 1; i < headers.size(); i ++) {
                            if (Objects.isNull(newTable.getRow(0).getCell(i))) {
                                newTable.getRow(0).createCell();
                            }
                            XWPFTableCell cell = newTable.getRow(0).getCell(i);
                            XWPFRun cellRun = cell.addParagraph().createRun();
                            if (StringUtils.isNotBlank(fontFamily)) {
                                cellRun.setFontFamily(fontFamily);
                            }
                            if (Objects.nonNull(fontSize)) {
                                cellRun.setFontSize(fontSize);
                            }
                            cellRun.setText(headers.get(i), 0);
                        }
                        // insert data
                        for (int i = 0; i < rowValues.size(); i ++) {
                            XWPFTableRow row = newTable.createRow();
                            CTVMerge vmerge1 = CTVMerge.Factory.newInstance();
                            vmerge1.setVal(STMerge.CONTINUE);
                            List<String> rowDataList = rowValues.get(i);
                            for (int j = 0; j < rowDataList.size(); j ++) {
                                if (mergeFirstColumn) {
                                    if (j == 0) {
                                        row.getCell(j).getCTTc().addNewTcPr().setVMerge(vmerge1);
                                    } else {
                                        XWPFTableCell cell = row.getCell(j);
                                        XWPFRun cellRun = cell.addParagraph().createRun();
                                        if (StringUtils.isNotBlank(fontFamily)) {
                                            cellRun.setFontFamily(fontFamily);
                                        }
                                        if (Objects.nonNull(fontSize)) {
                                            cellRun.setFontSize(fontSize);
                                        }
                                        cellRun.setText(rowDataList.get(j), 0);
                                    }
                                } else {
                                    XWPFTableCell cell = row.getCell(j);
                                    XWPFRun cellRun = cell.addParagraph().createRun();
                                    if (StringUtils.isNotBlank(fontFamily)) {
                                        cellRun.setFontFamily(fontFamily);
                                    }
                                    if (Objects.nonNull(fontSize)) {
                                        cellRun.setFontSize(fontSize);
                                    }
                                    cellRun.setText(rowDataList.get(j), 0);
                                }
                            }
                        }
                    }
                    run.setText("", 0);
//                    iterator.remove();
//                    paragraph.removeRun(0);
                }
            }
        }
    }
    void handleImgValue(XWPFDocument document, String key, InputStream imgInputStream) throws IOException, InvalidFormatException {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        String rKey = "{{" + key + "}}";
        try {
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (StringUtils.isNotBlank(text) && text.indexOf(rKey) != -1) {
                        run.addBreak();
                        run.addPicture(imgInputStream, Document.PICTURE_TYPE_PNG, "", Units.toEMU(300), Units.toEMU(200));
                        run.addBreak(BreakType.TEXT_WRAPPING);
                        run.setText("", 0);
                    }
                }
            }
        } finally {
            imgInputStream.close();
        }
    }
}
