package com.wanghui.blog.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.entity.Category;
import com.wanghui.blog.entity.Link;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.CategoryService;
import com.wanghui.blog.service.UserService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.util.WebUtils;
import com.wanghui.blog.vo.ExcelCategoryVo;
import com.wanghui.blog.vo.PageVo;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表控制层
 *
 * @author wanghui
 * @since 2023-03-23 16:41:37
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;
    /**
     * 查询所有分类信息
     * @return 所有数据
     */
    @GetMapping("/listAllCategory")
    @SelfDefinedSystemLog(BusinessName="查询所有分类信息")
    @ApiOperation(value = "文章分类",notes = "查询所有分类信息")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    /**
     * 导出分类信息（Excel）
     */
    @PreAuthorize("@permission.hasPermission('content:category:export')")//
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * 查询分页后的分类信息列表
     * */
    @GetMapping("/list")
    @SelfDefinedSystemLog(BusinessName = "查询分页后的分类信息列表")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, String categoryName, String status){
        return categoryService.pageCategoryList(pageNum,pageSize,categoryName,status);
    }

    /**
     * 新增分类信息
     * */
    @PostMapping
    @SelfDefinedSystemLog(BusinessName = "新增分类信息")
    public ResponseResult save(@RequestBody Category category){
        boolean saveFlag = categoryService.save(category);
        if(saveFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 删除分类信息
     * */
    @DeleteMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "删除分类信息")
    public ResponseResult delete(@PathVariable("id") String categoryId){
        String[] categoryIdArray = categoryId.split(",");
        //将String类型的数组转化成Long类型的集合
        List<Long> categoryIds = Arrays.asList(categoryIdArray).stream()
                .map(categoryListId -> Long.valueOf(categoryListId))
                .collect(Collectors.toList());
        return ResponseResult.okResult(categoryService.removeByIds(categoryIds));
    }

    /**
     * 根据分类id获取分类信息
     * */
    @GetMapping("/{id}")
    @SelfDefinedSystemLog(BusinessName = "根据分类id获取分类信息")
    public ResponseResult selectLinkById(@PathVariable("id") Long categoryId){
        return categoryService.selectCategoryById(categoryId);
    }

    /**
     * 修改分类信息
     * */
    @PutMapping
    @SelfDefinedSystemLog(BusinessName = "修改分类信息")
    public ResponseResult updateLinkById(@RequestBody Category category){
        boolean updateFlag = categoryService.updateById(category);
        if(updateFlag){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }


}

