package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: 晴空๓
 * @Date: 2025/1/28 19:45
 * @Version: 1.0
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setmealDishMapper;

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表中插入一条数据
        dishMapper.insert(dish);
        Long dishId = dish.getId();


        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            // 向口味表中插入 n 条数据
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageHelperQuery(dishPageQueryDTO);

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());
        log.info("分页查询结果为[{}]", pageResult);
        return Result.success(pageResult);


//        // 计算偏移量
//        int offset = (dishPageQueryDTO.getPage() - 1) * dishPageQueryDTO.getPageSize();
//        // 查询当前页的数据
//        List<DishVO> dishVOList = dishMapper.pageQuery(dishPageQueryDTO.getPageSize(), offset, dishPageQueryDTO.getName(),
//                dishPageQueryDTO.getCategoryId(), dishPageQueryDTO.getStatus());
//        // 查询数据库中的总条数
//        int total = dishMapper.getTotalSize();
//        PageResult pageResult = new PageResult();
//        pageResult.setTotal(total);
//        pageResult.setRecords(dishVOList);
//        log.info("分页查询结果为[{}]", pageResult);
//        return Result.success(pageResult);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否启售中
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断当前菜品是否被套餐关联
        List<Long> setMealIds = setmealDishMapper.getSetMealIdsByDishIds(ids);
        if (setMealIds != null && setMealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品表中的菜品数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            // 删除菜品关联的口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }
        // 代码优化：将多次数据库操作合并为批量操作，减少数据库连接的开销。
        dishMapper.deleteBatchByIds(ids);
        dishFlavorMapper.deleteBatchByDishIds(ids);
    }
}
