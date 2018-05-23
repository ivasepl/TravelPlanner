package com.tp.services;

import com.tp.jpa.UsersEntity;

public interface UserService{
     UsersEntity findByUsername(String username);
     void saveUser(UsersEntity usersEntity);
}
