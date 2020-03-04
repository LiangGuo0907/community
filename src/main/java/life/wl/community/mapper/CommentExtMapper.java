package life.wl.community.mapper;

import life.wl.community.model.Comment;


public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}