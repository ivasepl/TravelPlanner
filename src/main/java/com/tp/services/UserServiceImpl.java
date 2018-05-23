package com.tp.services;

import com.tp.jpa.UsersEntity;
import com.tp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsersEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(UsersEntity usersEntity) {
            usersEntity.setPassword(passwordEncoder.encode(usersEntity.getPassword()));
            usersEntity.setActive(true);
            userRepository.save(usersEntity);
    }

}
