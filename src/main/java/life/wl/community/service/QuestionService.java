package life.wl.community.service;

import life.wl.community.dto.PaginationDTO;
import life.wl.community.dto.QuestionDTO;
import life.wl.community.mapper.QuestionMapper;
import life.wl.community.mapper.UserMapper;
import life.wl.community.model.Question;
import life.wl.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page,Integer size) {
        Integer totalCount = questionMapper.count();//分页总数
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size + 1;
        }
        if (page<1){
            page = 1;
        }else if(page > totalPage){
            page = totalPage;
        }
        Integer offset = size * (page - 1);
        List<Question> questionList = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
            questionDTO.setTime(getTime(question.getGmtCreate()));
        }
        paginationDTO.setQuestions(questionDTOList);
        paginationDTO.setPagination(totalPage,page,size);
        return paginationDTO;
    }


    //时间戳转换成时间
    private String getTime(Long createTime){
        //这个是你要转成后的时间的格式
        SimpleDateFormat sdff=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间戳转换成时间
        return sdff.format(new Date(createTime));
    }
}
