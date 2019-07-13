package com.songhut.songhut.dao;

import com.songhut.songhut.model.TransferTask;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TransferTaskDAO {
    @Insert("insert into transfer_task (f_id, r_id, u_id) values " +
            "   (#{f_id,jdbcType=INTEGER},#{r_id,jdbcType=INTEGER},#{u_id,jdbcType=INTEGER})")
    void addTask(TransferTask transferTask);

    @Update("update transfer_task set file_path = #{file_path,jdbcType=VARCHAR}, " +
            "   is_done = #{is_done,jdbcType=INTEGER} where f_id = #{f_id,jdbcType=INTEGER} and " +
            "    r_id = #{r_id,jdbcType=INTEGER} and u_id = #{u_id,jdbcType=INTEGER}")
    void updateTask(TransferTask transferTask);

    @Select("select * from transfer_task where f_id = #{f_id,jdbcType=INTEGER} and " +
            "    r_id = #{r_id,jdbcType=INTEGER} and u_id = #{u_id,jdbcType=INTEGER}")
    List<TransferTask> selectTask(TransferTask transferTask);

    @Delete("delete from transfer_task where f_id = #{f_id,jdbcType=INTEGER} and " +
            "    r_id = #{r_id,jdbcType=INTEGER} and u_id = #{u_id,jdbcType=INTEGER}")
    void deleteTask(TransferTask transferTask);
}
