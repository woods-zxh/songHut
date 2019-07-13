package com.songhut.songhut.dao;

import com.songhut.songhut.model.Repository;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@org.springframework.stereotype.Repository
public interface RepositoryDAO {
    @Insert("insert into repository (name, create_time, introduce, is_public) values" +
            "   (#{name, jdbcType=VARCHAR}, #{create_time,jdbcType=TIMESTAMP }, " +
            "   #{introduce,jdbcType=VARCHAR}, #{is_public,jdbcType=INTEGER})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="r_id", before=false, resultType=int.class)
    void addRepository(Repository repository);

    @Select("select * from repository where r_id = #{r_id,jdbcType=INTEGER}")
    List<Repository> getRepository(@Param("r_id")Integer rid);


    @Update("update repository set img = #{img,jdbcType=INTEGER} where r_id = #{r_id,jdbcType=INTEGER}")
    void updateImg(@Param("img") Integer img, @Param("r_id") Integer rid);

    @Update("update repository set type_1 = #{type_1,jdbcType=INTEGER} where r_id = #{r_id,jdbcType=INTEGER}")
    void updateFile1(@Param("type_1") Integer type_1, @Param("r_id") Integer rid);

    @Update("update repository set type_2 = #{type_2,jdbcType=INTEGER} where r_id = #{r_id,jdbcType=INTEGER}")
    void updateFile2(@Param("type_2") Integer type_2, @Param("r_id") Integer rid);

    @Update("update repository set type_3 = #{type_3,jdbcType=INTEGER} where r_id = #{r_id,jdbcType=INTEGER}")
    void updateFile3(@Param("type_3") Integer type_3, @Param("r_id") Integer rid);

    @Update("update repository set type_4 = #{type_4,jdbcType=INTEGER} where r_id = #{r_id,jdbcType=INTEGER}")
    void updateFile4(@Param("type_4") Integer type_4, @Param("r_id") Integer rid);

}
