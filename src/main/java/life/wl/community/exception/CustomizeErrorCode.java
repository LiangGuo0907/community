package life.wl.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUSETION_NOT_FOUND(2001,"您查询的问题不存在，思考一下再试试哦！"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或者评论回复"),
    NO_LOGIN(2003,"当前操作需要登陆，请登陆后重试！"),
    SYS_ERROR(2004,"服务器冒烟了，请稍后再试试！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006,"评论不存在！"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空！"),
    READ_NOTIFICATION_FAIL(2008,"兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009,"消息莫非不翼而飞了？"),
    IMAGE_TOO_BIG(2010,"图片大小不能超过1M");
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
