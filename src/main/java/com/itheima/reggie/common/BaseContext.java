package com.itheima.reggie.common;

/**
 * BaseContext
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @author fj
 * @date 2022/10/8 12:56
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
