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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page",defaultValue = "1")Integer page,
            @RequestParam(name = "size",defaultValue = "2")Integer size){
        //验证用户是否已登录，如果登录就显示用户名
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        //从question数据表中查出发布的问题的相关数据
        PaginationDTO paginationDTO = questionService.list(page,size);
        model.addAttribute("pagination",paginationDTO);
        return "index";
    }

}
