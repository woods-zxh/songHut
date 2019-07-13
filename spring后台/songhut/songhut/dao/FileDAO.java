package com.songhut.songhut.dao;

import com.songhut.songhut.model.File;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@org.springframework.stereotype.Repository
public interface FileDAO {
    @Insert("insert into file (filepath, filetype, load_time, introduce, is_public) values" +
            "   (#{filePath, jdbcType=VARCHAR}, #{fileType,jdbcType=INTEGER }," +
            "   #{load_time,jdbcType=TIMESTAMP}, #{introduce,jdbcType=VARCHAR}, #{is_public,jdbcType=INTEGER}) ")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="f_id", before=false, resultType=int.class)
    void addFile(File file);

    @Select("select * from file where f_id = #{f_id,jdbcType=INTEGER}")
    List<File> getFile(@Param("f_id")Integer fid);

    @Update("update file set filepath = #{filepath,jdbcType=VARCHAR}, load_time = #{load_time,jdbcType=TIMESTAMP}" +
            " where f_id = #{f_id,jdbcType=INTEGER}")
    void updateFile(@Param("filepath")String filepath, @Param("load_time") Timestamp load_time, @Param("f_id")Integer fid);
}