/**
 * 提交一级评论
 */
function post() {
    var question_id = $("#question_id").val();
    var content = $("#comment_content").val();
    commentToTarget(question_id,1,content);
}

/**
 * 提交二级评论
 */
function comments(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    commentToTarget(commentId,2,content);
}

/**
 * 一级和二级评论调用的方法
 * 一级评论传进去的是问题id，类型为1(一级评论),评论内容
 * 二级评论传递的是一级评论的id，类型2(二级评论)，评论内容
 * @param targetId
 * @param type
 * @param comment
 */
function commentToTarget(targetId, type,content) {
    if (!content){
        alert("请输入回复内容！");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":targetId,
            "comment":content,
            "type":type
        }),
        success:function (response) {
            if (response.code == 200){
                $("#comment_section").hide();
                window.location.reload();
            }else {
                if (response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if (isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=8ba234fbb6480fd2c140&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable","true");
                    }
                }else {
                    alert(response.message);
                }
            }
        },
        dataType:"json"
    });
}


/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    //如果存在，则折叠二级评论并删除状态
    if (collapse){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else {
        var subCommentContainer = $("#comment-"+id);
        if (subCommentContainer.children().length != 1){
            comments.addClass("in");//展开二级评论
            e.setAttribute("data-collapse","in");//标记二级评论状态
            e.classList.add("active");
        }else {
            $.getJSON( "/comment/"+id, function( data ) {

                $.each(data.data.reverse(), function(index,comment) {
                    var mediaLeftElement = $("<div>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div>",{
                        "class":"media-body"
                    }).append($("<h6/>",{
                        "class":"media-heading userNameText",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.comment
                    })).append($("<div/>",{
                        "class":"comment-menu"
                    })).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD HH:mm:ss")
                    }));

                    var mediaElement = $("<div>",{
                        "class":"media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

                comments.addClass("in");//展开二级评论
                e.setAttribute("data-collapse","in");//标记二级评论状态
                e.classList.add("active");
            });
        }
    }
}


function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1){
        if (previous){
            $("#tag").val(previous+','+value);
        }else {
            $("#tag").val(value);
        }
    }
}

function isShowSelectTag() {
    $("#select-tags").show();
}