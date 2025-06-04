package com.tianyu.property.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtil {

    /**
     * 导出Excel
     *
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @param data 数据列表
     * @param clazz 实体类
     */
    public static <T> void exportExcel(HttpServletResponse response, String fileName, List<T> data, Class<T> clazz) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(true);
        writer.write(data, true);
        
        writer.flush(response.getOutputStream(), true);
        writer.close();
    }

    /**
     * 导入Excel
     *
     * @param file Excel文件
     * @param clazz 实体类
     * @return 数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) throws IOException {
        ExcelReader reader = cn.hutool.poi.excel.ExcelUtil.getReader(file.getInputStream());
        List<T> list = reader.readAll(clazz);
        reader.close();
        return list;
    }
}