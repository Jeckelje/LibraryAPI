package com.modsen.booktrackerservice.repository;

import com.modsen.booktrackerservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);

    User findUserByUsername(String username);

}
