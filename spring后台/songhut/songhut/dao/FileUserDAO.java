package com.songhut.songhut.dao;


import com.songhut.songhut.model.Repository;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
@org.springframework.stereotype.Repository
public interface FileUserDAO {
    @Insert("insert into file_user (u_id, f_id) values" +
            "   ( #{u_id, jdbcType=INTEGER}, #{f_id,jdbcType=INTEGER } )")
    public void addFileUser(@Param("u_id") Integer u_id, @Param("f_id") Integer f_id);
}