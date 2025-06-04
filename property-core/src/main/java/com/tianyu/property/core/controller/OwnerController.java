package com.tianyu.property.core.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianyu.property.common.annotation.RequirePermission;
import com.tianyu.property.common.api.CommonResult;
import com.tianyu.property.core.domain.Owner;
import com.tianyu.property.core.service.OwnerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 业主信息控制器
 */
@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Resource
    private OwnerService ownerService;

    @PostMapping
    @RequirePermission("owner:create")
    public CommonResult<Void> create(@RequestBody Owner owner) {
        ownerService.create(owner);
        return CommonResult.success(null);
    }

    @PutMapping
    @RequirePermission("owner:update")
    public CommonResult<Void> update(@RequestBody Owner owner) {
        ownerService.update(owner);
        return CommonResult.success(null);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("owner:delete")
    public CommonResult<Void> delete(@PathVariable Long id) {
        ownerService.delete(id);
        return CommonResult.success(null);
    }

    @GetMapping("/{id}")
    @RequirePermission("owner:query")
    public CommonResult<Owner> get(@PathVariable Long id) {
        return CommonResult.success(ownerService.get(id));
    }

    @GetMapping("/page")
    @RequirePermission("owner:query")
    public CommonResult<Page<Owner>> page(
            @RequestParam Long projectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        return CommonResult.success(ownerService.page(projectId, name, phone, pageNum, pageSize));
    }
}