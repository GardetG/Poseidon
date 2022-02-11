package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for User entity.
 */
public interface UserRepository extends JpaRepository<User, Integer>,
    JpaSpecificationExecutor<User> {

}
