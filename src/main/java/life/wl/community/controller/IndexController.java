package life.wl.community.controller;

import life.wl.community.dto.PaginationDTO;
import life.wl.community.dto.QuestionDTO;
import life.wl.community.mapper.QuestionMapper;
import life.wl.community.mapper.UserMapper;
import life.wl.community.model.Question;
import life.wl.community.model.User;
import life.wl.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
            Model model,
            @RequestParam(name = "page",defaultValue = "1")Integer page,
            @RequestParam(name = "size",defaultValue = "8")Integer size){

        //从question数据表中查出发布的问题的相关数据
        PaginationDTO paginationDTO = questionService.list(page,size);
        model.addAttribute("pagination",paginationDTO);
        return "index";
    }

}
