package com.sakura.backend.repository;

import com.sakura.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {

    /* 查询需要登录的成员信息 */
    User findMemberByAccountAndPasswordAndAuthority(String account, String password, String authority);
}
