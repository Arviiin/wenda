package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private static final Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/","/index"} ,method = {RequestMethod.GET})
    public String index(Map map) {
        /*List<Question> questionList = questionService.getLatestQuestion(0, 0, 10);
        map.put("questions" ,questionList);*/
        return "index" ;
    }

}
