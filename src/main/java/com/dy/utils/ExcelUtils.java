package com.dy.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Author: dy
 * @Date: 2024/4/21 21:01
 * @Description: Excel 工具类
 */
@Slf4j
public class ExcelUtils {

    public static String excelToCsv(MultipartFile multipartFile) {

//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:网站用户增长趋势.xlsx");
//        } catch (FileNotFoundException e) {
//            log.info("Excel 转 CSV 错误!");
//            throw new RuntimeException(e);
//        }

        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误");
            throw new RuntimeException(e);
        }
        //  表格中没有数据
        if (CollUtil.isEmpty(list)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        //  读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        List<String> headerList = headerMap.values().stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList());
        stringBuilder.append(StrUtil.join(",", headerList)).append("\n");

        for (int i = 1; i < list.size(); i ++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList());
            stringBuilder.append(StrUtil.join(",", dataList)).append("\n");
//            System.out.println(join);
        }

        //System.out.println(stringBuilder);



        return stringBuilder.toString();
    }


}
