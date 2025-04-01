package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author: 晴空๓
 * @Date: 2025/3/31 21:39
 * @Version: 1.0
 */
@Mapper
public interface SetMealDishMapper {

    /**
     * 根据菜品ID查询套餐ID
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);
}
