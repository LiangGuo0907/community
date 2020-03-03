package life.wl.community.dto;

import life.wl.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private String tag;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
    private String time;
}
