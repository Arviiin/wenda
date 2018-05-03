package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private static final Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;


    @RequestMapping(path = {"/user/{userId}"} ,method = {RequestMethod.GET})
    public String userIndex(Map map, @PathVariable("userId") int userId) {
        map.put("vos",getQuestion(userId ,0,10));
        return "index";
    }

    @RequestMapping(path = {"/","/index"} ,method = {RequestMethod.GET})
    public String index(Map map) {
        map.put("vos" ,getQuestion(0,0,10));
        return "index" ;
    }


    private List<ViewObject> getQuestion(int userId,int offset, int limit){
        List<Question> questionList = questionService.getLatestQuestion(userId ,offset,limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }




}
