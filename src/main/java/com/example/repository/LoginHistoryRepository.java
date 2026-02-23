package com.example.repository;

import com.example.model.LoginHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findByUsername(String username);
    @Modifying
    @Query("""
       update LoginHistory l
       set l.currentSession = false
       where l.username = :username
       """)
    void updateCurrentSession(@Param("username") String username);
    boolean existsByUsernameAndDeviceId(String username, String deviceId);
    List<LoginHistory> findByUsernameOrderByLoginTimeDesc(String username);

    LoginHistory findTopByUsernameOrderByLoginTimeDesc(String username);
}
