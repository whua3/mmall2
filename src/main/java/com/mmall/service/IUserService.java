package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @author: whua
 * @create: 2019/04/26 17:07
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(Integer userId,String passwordOld,String passwordNew);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getinformation(Integer userId);

    //backend

    ServerResponse<String> registerAdmin(User user);

}
