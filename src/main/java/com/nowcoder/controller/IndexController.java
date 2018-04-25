package com.nowcoder.controller;

import com.nowcoder.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {

    @RequestMapping(path = {"/","/index"})
    @ResponseBody//直接返回字符串
    public String index(HttpSession httpSession) {
        return "Hello NowCoder" + httpSession.getAttribute("msg") ;
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key" ,required = false) String key) {
        return String.format("Profile Page of %s / %d, t:%d k:%s",groupId,userId,type,key);
    }


    @RequestMapping(path = {"/ftl"},method = {RequestMethod.GET})
    //返回模板文件
    public String template(Map<String,Object> map) {
        map.put("value1","vvvvv1");
        map.put("value3","yyyyy1");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        map.put("colors",colors);

        Map<String,String> hashMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            hashMap.put(String.valueOf(i),String.valueOf(i*i));
        }
        map.put("hashMap",hashMap);
        map.put("user",new User("LEE"));
        return "home";
    }


    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody
    //返回文本
    public String request(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession,
                           @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId );
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" +request.getHeader(name) + "<br>");
        }

        if(request.getCookies() != null){
            for (Cookie cookie: request.getCookies()) {
                sb.append("Cookie:" +cookie.getName() + "value: "+ cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");


        response.addHeader("nowcoderId" ,"hello");
        response.addCookie(new Cookie("username", "nowcoder"));
        return sb.toString();


    }

    /*@RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,
                           HttpSession httpSession) {
        httpSession.setAttribute("msg","jump from redirect");
        return "redirect:/";
    }*/

    @RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        RedirectView red = new RedirectView("/",true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }


    //1.29

}
