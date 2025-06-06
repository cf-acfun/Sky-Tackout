package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author 晴空
 */
@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    void deleteBatchByDishIds(List<Long> ids);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getDishFlavorsByDishId(Long dishId);
}
