package com.songhut.songhut.dao;


import com.songhut.songhut.model.Repository;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@org.springframework.stereotype.Repository
public interface RepositoryFileDAO {
    @Insert("insert into repository_file (f_id, r_id) values" +
            "   ( #{f_id, jdbcType=INTEGER}, #{r_id,jdbcType=INTEGER } )")
    void addRepositoryFile(@Param("f_id") Integer f_id, @Param("r_id") Integer r_id);

    @Select("select f_id from repository_file where r_id = #{r_id,jdbcType=INTEGER}")
    List<Integer> getFileOfRepository(@Param("r_id")Integer r_id);
}