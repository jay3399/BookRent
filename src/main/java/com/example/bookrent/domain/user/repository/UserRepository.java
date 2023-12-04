package com.example.bookrent.domain.user.repository;

import com.example.bookrent.domain.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.accountStatus = 'INACTIVE' and u.createAt<:date")
    List<User> findUsersBeforeDate(@Param("date") LocalDateTime date);


}
