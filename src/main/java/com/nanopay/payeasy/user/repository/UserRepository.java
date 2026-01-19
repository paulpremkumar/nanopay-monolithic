package com.nanopay.payeasy.user.repository;

import com.nanopay.payeasy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByMobile(String mobile);

    UserEntity findByEmail(String email);
}
