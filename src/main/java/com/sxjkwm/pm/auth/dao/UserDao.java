package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.wxUserId=?1 AND u.isDeleted=0 ")
    User findUserByWxUserId(String wxUserId);

    @Query(value = "SELECT u FROM User u WHERE u.mobile=?1 AND u.isDeleted=0 ")
    User findUserByMobile(String mobile);

    @Query(value = "SELECT u FROM User u WHERE u.isDeleted = 0")
    List<User> fetchAvailableUsers();

}
