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
            //  如果是 File 类型的文件, 可以直接读入, 也不用处理异常了
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

        //  这里表头和数据我们是可以一块读取的, 这里分别读取, 或许扩展性更强吧

        //  读取表头
        //  这里将 map 强转为 LinkedHashMap (保证数据的顺序性)
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);


        //  这里 key 对应的是编号, value 对应的是表格中的数据, 我们首先取出所有的 value
        //  然后使用 stream 中的 filter 进行去重, 最后将它收集为 list
        List<String> headerList = headerMap.values().stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList());
        stringBuilder.append(StrUtil.join(",", headerList)).append("\n");

        for (int i = 1; i < list.size(); i ++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList());
            stringBuilder.append(StrUtil.join(",", dataList)).append("\n");
        }
        return stringBuilder.toString();
    }


}
