package life.wl.community.controller;

import life.wl.community.dto.NotificationDTO;
import life.wl.community.dto.PaginationDTO;
import life.wl.community.enums.NotificationEnum;
import life.wl.community.mapper.NotificationMapper;
import life.wl.community.model.Notification;
import life.wl.community.model.User;
import life.wl.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id,user);
        if (NotificationEnum.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }
    }
}
