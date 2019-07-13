package com.songhut.songhut.dao;


import com.songhut.songhut.model.Repository;
import com.songhut.songhut.model.RepositoryUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@org.springframework.stereotype.Repository
public interface RepositoryUserDAO {
    @Insert("insert into repository_user (u_id, r_id) values" +
            "   ( #{u_id, jdbcType=INTEGER}, #{r_id,jdbcType=INTEGER } )")
    void addRepositoryUser(@Param("u_id") Integer u_id, @Param("r_id") Integer r_id);

    @Select("select r_id from repository_user where u_id = #{u_id,jdbcType=INTEGER}")
    List<Integer> getRepositoryOfUser(@Param("u_id")Integer u_id);

    @Select("select * from repository_user where u_id = #{u_id,jdbcType=INTEGER} and r_id = #{r_id,jdbcType=INTEGER}")
    List<RepositoryUser> getRelation(@Param("u_id")Integer u_id,@Param("r_id")Integer r_id);
}
