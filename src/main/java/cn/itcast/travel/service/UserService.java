package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    public boolean registerUser(User user);

    boolean active(String code);

    User login(User user);
}
