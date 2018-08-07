package com.tp.repositories;


import com.tp.jpa.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<UsersEntity,Integer> {
        UsersEntity findByUsername(String username);
}
