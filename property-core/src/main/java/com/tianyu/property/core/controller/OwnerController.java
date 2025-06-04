package com.tianyu.property.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.common.annotation.RequirePermission;
import com.tianyu.property.common.api.CommonResult;
import com.tianyu.property.common.util.ExcelUtil;
import com.tianyu.property.core.domain.Owner;
import com.tianyu.property.core.service.OwnerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 业主信息控制器
 */
@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Resource
    private OwnerService ownerService;

    // ... 其他方法保持不变 ...

    @PostMapping("/import")
    @RequirePermission("owner:import")
    public CommonResult<Void> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<Owner> ownerList = ExcelUtil.importExcel(file, Owner.class);
        for (Owner owner : ownerList) {
            ownerService.create(owner);
        }
        return CommonResult.success(null);
    }

    @GetMapping("/export")
    @RequirePermission("owner:export")
    public void exportExcel(HttpServletResponse response,
                          @RequestParam Long projectId,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String phone) throws IOException {
        List<Owner> ownerList = ownerService.list(projectId, name, phone);
        ExcelUtil.exportExcel(response, "业主信息", ownerList, Owner.class);
    }
}