package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @Description: 通用接口
 * @Author: ChenFeng
 * @Date: 2025/1/12 20:33
 * @Version: 1.0
 */

/**
 *      @RestController 是 Spring MVC 中一个用于定义 RESTful Web 服务的注解，
 *  它结合了 @Controller 和 @ResponseBody 两个注解的功能
 *  使用 @RestController 标注的类下的所有方法返回的数据直接写入 HTTP 响应体中，
 *  这是因为这些方法隐式地带有 @ResponseBody 注解。
 *
 */
@RestController
@RequestMapping("admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传开始[{}]", file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新的文件名
            String newFileName = UUID.randomUUID() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), newFileName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.info("文件上传失败", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
