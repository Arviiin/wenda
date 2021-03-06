package com.nowcoder.controller;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SensitiveService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger= LoggerFactory.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try {

            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                //如果hostHolder不为空，即用户登陆状态
                comment.setUserId(hostHolder.getUser().getId());
            }else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);

            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);

            //添加完评论后，要把对应的问题的计数加上
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);

        }catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());

        }
        return "redirect:/question/" + questionId;
    }
}
