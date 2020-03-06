package life.wl.community.service;

import life.wl.community.dto.PaginationDTO;
import life.wl.community.dto.QuestionDTO;
import life.wl.community.exception.CustomizeErrorCode;
import life.wl.community.exception.CustomizeException;
import life.wl.community.mapper.QuestionExtMapper;
import life.wl.community.mapper.QuestionMapper;
import life.wl.community.mapper.UserMapper;
import life.wl.community.model.Question;
import life.wl.community.model.QuestionExample;
import life.wl.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page,Integer size) {
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());//分页总数
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
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
            questionDTO.setTime(getTime(question.getGmtCreate()));
        }
        paginationDTO.setData(questionDTOList);
        paginationDTO.setPagination(totalPage,page,size);
        return paginationDTO;
    }


    public PaginationDTO list(Long userId, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);//分页总数
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
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
            questionDTO.setTime(getTime(question.getGmtCreate()));
        }
        paginationDTO.setData(questionDTOList);
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

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUSETION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setTime(getTime(question.getGmtCreate()));
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 假如id为空，则是创建问题
     * 不为空，则是修改问题
     * @param question
     */
    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updateNum = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updateNum != 1){
                throw new CustomizeException(CustomizeErrorCode.QUSETION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }

    /**
     * 根据问题id和标签获取类似的tag
     * @param questionDTO
     * @return
     */
    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(),",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questionList = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionTags = questionList.stream().map(q -> {
            QuestionDTO questionDTONew = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTONew);
            return questionDTONew;
        }).collect(Collectors.toList());
        return questionTags;
    }
}
