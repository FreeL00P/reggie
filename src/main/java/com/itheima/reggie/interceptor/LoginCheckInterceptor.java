//package com.itheima.reggie.interceptor;
//
//import com.itheima.reggie.common.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.objenesis.instantiator.annotations.Instantiator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * LoginCheckInterceptor
// *
// * @author fj
// * @date 2022/10/5 22:52
// */
////@Slf4j
////@Component
//public class LoginCheckInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("preHandle--->>>>>");
//        Object loginUser = request.getSession().getAttribute("employee");
//        if (loginUser == null) {
//            //未登录，返回登陆页
//            R.error("你没有权限");
//            request.getRequestDispatcher("/backend/page/login/login.html").forward(request, response);
//            return false;
//        } else {
//            //放行
//            return true;
//        }
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.info("postHandle--->>>>>");
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        log.info("afterHandle--->>>>>");
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }
//}
