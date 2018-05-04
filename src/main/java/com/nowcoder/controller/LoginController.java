package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    //注册
    @RequestMapping(path = {"/reg/"} ,method = {RequestMethod.POST})
    public String reg(Map map,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response){
        try{
            Map<String, String> regMap = userService.register(username, password);
            if(regMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", regMap.get("ticket"));
                cookie.setPath("/");//将ticket下发到客户端
                response.addCookie(cookie);
                return "redirect:/";//跳到首页
            }else{
                map.put("msg", regMap.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return "login";
        }

    }

    //登陆
    @RequestMapping(path = {"/login/"} ,method = {RequestMethod.POST})
    public String login(Map map,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String, String> regMap = userService.login(username, password);
            if(regMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", regMap.get("ticket"));
                cookie.setPath("/");//将ticket下发到客户端
                response.addCookie(cookie);
                return "redirect:/";//跳到首页
            }else{
                map.put("msg", regMap.get("msg"));
                return "login";
            }

        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return "login";
        }

    }

    //登陆注册页面显示
    @RequestMapping(path = {"/reglogin"} ,method = {RequestMethod.GET})
    public String reg(Map map){
        return "login";
    }

}
