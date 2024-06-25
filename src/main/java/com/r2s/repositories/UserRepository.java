package com.r2s.repositories;

import com.r2s.entities.UserEntity;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.userName = :userName")
    Optional<UserEntity> findByUserName(@PathParam("userName") String userName);

}
