package life.wl.community.cache;

import life.wl.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get(){
        ArrayList<TagDTO> tagList = new ArrayList<>();
        TagDTO programs = new TagDTO();
        programs.setCategoryName("开发语言");
        programs.setTags(Arrays.asList("Java","c#","c++","js","html","php","css","node","go","python","HTML5","swift","sass","golang","objective-c" ,"typescript","shell","ruby","bash","Less","asp.net" ,"Lua" ,"scala","coffescript","actionscript" ,"rust","erlang" ,"perl"));
        tagList.add(programs);

        TagDTO frameworks = new TagDTO();
        frameworks.setCategoryName("平台框架");
        frameworks.setTags(Arrays.asList("laravel" ,"spring", "express", "django","flask" ,"yii","ruby-on-rails" ,"tornado" ,"koa" ,"struts"));
        tagList.add(frameworks);

        TagDTO servers = new TagDTO();
        servers.setCategoryName("服务器");
        servers.setTags(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存tomcat","负载均衡","unix","hadoop","windows-server"));
        tagList.add(servers);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql memcached","sqlserver","postgresql","sqlite"));
        tagList.add(db);

        TagDTO tools = new TagDTO();
        tools.setCategoryName("开发工具");
        tools.setTags(Arrays.asList("git" ,"github","visual-studio-code","vim","sublime-text","xcode intellij-idea","eclipse","maven","ide","svn","visual-studio","atom emacs","textmate","hg"));
        tagList.add(tools);

        return tagList;
    }


    public static String filterValid(String tags){
        String[] tagsResult = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(tagsResult).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
