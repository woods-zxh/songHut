package com.songhut.songhut.dao;

import com.songhut.songhut.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface UserDAO {
    @Insert("insert into user (phone, password, " +
            "      email, nickname, `signature`, expired_time," +
            "      register_time )" +
            "    values (#{phone,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}," +
            "      #{email,jdbcType=VARCHAR}, #{nickname,jdbcType=VARCHAR}, #{signature,jdbcType=VARCHAR}, #{expired_time,jdbcType=TIMESTAMP}," +
            "      #{register_time,jdbcType=TIMESTAMP} )")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="u_id", before=false, resultType=int.class)
    void addUser(User user);

    @Update("update user set email=#{email,jdbcType=VARCHAR}, "+
            "       nickname=#{nickname,jdbcType=VARCHAR}"+
            "       where u_id = #{u_id,jdbcType=INTEGER}")
    void updateUser(User user);

    @Select("select `u_id`, phone, `password`, `email`, `nickname`, `signature`, expired_time, register_time, img"+
            "    from user" +
            "    where `signature` = #{signature,jdbcType=VARCHAR}")
    List<User> selectUserBySign(@Param("signature") String signature);

    @Select("select count(*)" +
            "    from user" +
            "    where `phone` = #{phone,jdbcType=VARCHAR}")
    int selectCount(@Param("phone") String phone);

    @Select("select `u_id`, phone, `password`, email, `nickname`, `signature`, expired_time, register_time, img " +
            "    from user" +
            "    where `phone` = #{phone,jdbcType=VARCHAR}")
    List<User> selectUserByPhone(@Param("phone") String phone);

    @Select("select `u_id`, phone, `password`, email, `nickname`, `signature`, expired_time, register_time, img " +
            "    from user" +
            "    where `u_id` = #{u_id,jdbcType=INTEGER}")
    List<User> selectUserByUid(@Param("u_id") Integer u_id);

    @Update("update user set expired_time = #{expiredTime,jdbcType=TIMESTAMP} where u_id = #{u_id,jdbcType=INTEGER}")
    void updateExpiredTime(@Param("expiredTime")Timestamp expiredTime, @Param("u_id") Integer uId);

    @Update("update user set `signature` = #{signature,jdbcType=VARCHAR} where u_id = #{u_id,jdbcType=INTEGER}")
    void updateToken(@Param("signature")String signature, @Param("u_id") Integer uId);

    @Update("update user set `img` = #{img,jdbcType=INTEGER} where u_id = #{u_id,jdbcType=INTEGER}")
    void updateImg(@Param("img")Integer img, @Param("u_id") Integer uId);


    @Update("update user set password = #{password,jdbcType=VARCHAR} where u_id = #{u_id,jdbcType=VARCHAR}")
    void updatePassword(@Param("password") String password, @Param("u_id") Integer u_id);

    @Update("update user set password = #{password,jdbcType=VARCHAR} where phone = #{phone,jdbcType=VARCHAR}")
    void forgetPassword(@Param("password") String password, @Param("phone") String phone);
}
