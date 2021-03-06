package com.nowcoder.service;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    UserService userService;

    public Question selectById(int id){
        return questionDAO.selectById(id);
    }

    public int addQuestion(Question question){
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));//将<script>alter("hi");</script>进行转义
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;//如果提交成功就>0
    }

    public List<Question> getLatestQuestion(int userId,int offset, int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }

    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }
}
