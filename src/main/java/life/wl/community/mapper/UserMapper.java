package life.wl.community.mapper;

import life.wl.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into USER (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from USER where token = #{token}")
    User findByToken(@Param("token")String token);

    @Select("select * from USER where id = #{id}")
    User findById(@Param("id")Integer id);

    @Select("select * from USER where account_id = #{accountId}")
    User findByAccountId(String accountId);

    @Update("update USER set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where account_id=#{accountId}")
    void updateByAccountId(User dbUser);
}
