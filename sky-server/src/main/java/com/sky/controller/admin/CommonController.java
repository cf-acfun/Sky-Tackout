package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class CommonController {


    public Result<String> upload(MultipartFile file) {

        return null;
    }

}
