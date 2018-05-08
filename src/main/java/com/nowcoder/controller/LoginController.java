package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
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

    //注册登陆页面显示
    @RequestMapping(path = {"/reglogin"} ,method = {RequestMethod.GET})
    public String regloginPage(Map<String,Object> map,
                               //@RequestParam(value = "next", required = false) String next,
                               @RequestParam(value = "next" ,required = true,defaultValue = "NO") String next
                               ){//不管是地址栏里的参数，还是表单里面的参数都可以用@RequestParam取得
        if(next!=null && !"NO".equals(next)){//太诡异了，required = false居然不行
            map.put("next",next);//把next的值埋在前端form表单里当提交表单时候就传了出去。<input type="hidden" name="next" value="${next}">
        }
        return "login";//踩坑记录渲染的是login.html，我改的是header.html,
    }

    //注册
    @RequestMapping(path = {"/reg/"} ,method = {RequestMethod.POST})
    public String reg(Map map,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      //@RequestParam(value = "next", required = false) String next,
                      @RequestParam(value = "next" ,required = true,defaultValue = "NO") String next,
                      HttpServletResponse response){
        try{
            Map<String, String> regMap = userService.register(username, password);
            if(regMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", regMap.get("ticket"));
                cookie.setPath("/");//将ticket下发到客户端
                response.addCookie(cookie);
                if(!StringUtils.isEmpty(next) && !"NO".equals(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";//跳到首页
            }else{
                map.put("msg", regMap.get("msg"));
                return "login.ftl";
            }
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return "login.ftl";
        }
    }

    //登陆
    @RequestMapping(path = {"/login/"} ,method = {RequestMethod.POST})
    public String login(Map map,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        //@RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "next" ,required = true,defaultValue = "NO") String next,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String, String> regMap = userService.login(username, password);
            if(regMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", regMap.get("ticket"));
                cookie.setPath("/");//将ticket下发到客户端
                response.addCookie(cookie);
                if(!StringUtils.isEmpty(next) && !"NO".equals(next)){//踩坑记录：因为没加！调试几个小时
                    return "redirect:"+next;
                }
                return "redirect:/";//跳到首页
            }else{
                map.put("msg", regMap.get("msg"));
                return "login.ftl";
            }
        }catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return "login.ftl";
        }
    }

    //退出
    @RequestMapping(path = {"/logout"} ,method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
