package com.songhut.songhut.util;

import com.songhut.songhut.controller.UserController;
import com.songhut.songhut.dao.RepositoryDAO;
import com.songhut.songhut.dao.UserDAO;
import com.songhut.songhut.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 数据持久化工具
 * @author Kun
 */
public class DaoTools {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RepositoryDAO repositoryDAO;

    public User getUserByToken(String token) {
        Jwts jwt = Jwts.build().unpack(token);
        List<User> users = userDAO.selectUserBySign(jwt.getSign());
        if(users.size()==0||users==null){
            return null;
        }else{
            return users.get(0);
        }
    }



    public boolean isExpired(User user){
        return user.getExpired_time().getTime()<new Date().getTime();
    }
}
