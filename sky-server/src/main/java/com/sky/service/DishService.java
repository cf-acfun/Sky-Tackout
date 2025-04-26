package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @Description:
 * @Author: 晴空๓
 * @Date: 2025/1/28 19:40
 * @Version: 1.0
 */

public interface DishService {

    /**
     * 新增菜品和口味功能
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据Id查询对应的口味数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 根据Id修改菜品基本信息和口味信息
     * @param dishDTO
     * @return
     */
    Result updateDishWithFlavors(DishDTO dishDTO);
}
