package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username); //返回数据库中这个username的数量

    // mybatis中当传一个以上的参数时，要在参数前面加上@Param注解，xml用的时候就用@Param注解配置的变量名
    User selectLogin(@Param("username") String username, @Param("password") String password);//登录之后返回数据库中的用户信息

    int checkEmail(String email);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    String selectPasswordByPrimaryKey(Integer userId);

    int updatePasswordByPrimaryKey(@Param("userId")Integer userId,@Param("passwordNew")String passwordNew);

    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);
}