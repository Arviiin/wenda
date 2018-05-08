package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

//发布问题是一个post的ajax（异步的js请求）请求，即：不是提交到一个页面，而是提交到后台，后台会返回一个json串，然后json串自己做一些动态的页面刷新或者说做一些动态的处理
@Controller
public class QuestionController {
    private static final Logger logger= LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody//限定返回json必须带code:0代表正确返回
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null){//如果为空为其设置匿名用户，以免出现问题
                //question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return WendaUtil.getJSONString(999);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }
}
