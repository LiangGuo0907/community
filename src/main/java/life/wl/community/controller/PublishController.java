package life.wl.community.controller;

import life.wl.community.cache.TagCache;
import life.wl.community.dto.QuestionDTO;
import life.wl.community.mapper.QuestionMapper;
import life.wl.community.mapper.UserMapper;
import life.wl.community.model.Question;
import life.wl.community.model.User;
import life.wl.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","用户未登录!");
            return "publish";
        }
        if (title == null || title == ""){
            model.addAttribute("error","标题不能为空!");
            return "publish";
        }
        if (description == null || description == ""){
            model.addAttribute("error","问题补充不能为空!");
            return "publish";
        }
        if (tag == null || tag == ""){
            model.addAttribute("error","标签不能为空!");
            return "publish";
        }

        String invalid = TagCache.filterValid(tag);
        if (StringUtils.isNotBlank(invalid)){
            model.addAttribute("error",invalid+"标签不存在或者错误！");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        question.setViewCount(0);
        question.setLikeCount(0);
        question.setCommentCount(0);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id")Long id,Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("title",questionDTO.getTitle());
        model.addAttribute("description",questionDTO.getDescription());
        model.addAttribute("tag",questionDTO.getTag());
        model.addAttribute("id",questionDTO.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
