package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.wxUserId=?1 AND u.isDeleted=0 ")
    User findUserByWxUserId(String wxUserId);

}
