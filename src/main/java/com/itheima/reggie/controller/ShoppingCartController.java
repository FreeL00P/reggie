package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * ShoppingCartController
 *
 * @author fj
 * @date 2022/10/10 20:45
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController{

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("");
        List<ShoppingCart> list = shoppingCartService.list();
        return R.success(list);
    }

    /**
     * 添加到购物车，已存在则数量加一
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        User user = (User)session.getAttribute("user");
        shoppingCart.setUserId(user.getId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart ::getUserId,user.getId());
        //添加到购物车的是菜品
        if (shoppingCart.getDishId()!=null) queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        //添加到购物车的是套餐
        if (shoppingCart.getSetmealId()!=null) queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        //shoppingCart.setNumber(shoppingCart.getNumber()+1);
        //判断购物车中是否已经存在
        ShoppingCart shop= shoppingCartService.getOne(queryWrapper);
        if (shop != null){
            //已存在
            //获取原先数量
            Integer number = shop.getNumber();
            shop.setNumber(number+1);
            shoppingCartService.updateById(shop);
        }else {
            //添加购物车
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shop=shoppingCart;
        }
       return R.success(shop);
    }

    /**
     * 减少购物车商品数量
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart, HttpSession session) {

        User user = (User)session.getAttribute("user");
        shoppingCart.setUserId(user.getId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //添加user_id作为条件
        queryWrapper.eq(ShoppingCart ::getUserId,user.getId());
        //添加到购物车的是菜品
        if (shoppingCart.getDishId()!=null) queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        //添加到购物车的是套餐
        if (shoppingCart.getSetmealId()!=null) queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        log.info("移除购物车 {}",shoppingCart);
        //把购物车中的商品查出来
        ShoppingCart shop = shoppingCartService.getOne(queryWrapper);
        //商品数量减一
        shop.setNumber(shop.getNumber()-1);
        //更新
        shoppingCartService.updateById(shop);
        return R.success(shoppingCart);
    }

    @DeleteMapping("/clean")
    public R<String> delete(HttpSession session){
        User user = (User)session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //添加user_id作为条件
        queryWrapper.eq(ShoppingCart ::getUserId,user.getId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清除购物车成功");
    }
}
