package life.wl.community.controller;

import life.wl.community.dto.CommentDTO;
import life.wl.community.dto.QuestionDTO;
import life.wl.community.enums.CommentTypeEnum;
import life.wl.community.service.CommentService;
import life.wl.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Long id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        questionService.incView(id);//累加阅读数
        model.addAttribute("question",questionDTO);//问题详情
        model.addAttribute("comments",comments);//评论列表
        model.addAttribute("relatedQuestions",relatedQuestions);//根据标签查询的相关问题
        return "question";
    }
}
