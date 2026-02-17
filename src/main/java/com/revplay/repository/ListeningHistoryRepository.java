package com.revplay.repository;

import com.revplay.entity.ListeningHistory;
import com.revplay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    // Full history (latest first)
    Page<ListeningHistory> findByUserOrderByPlayedAtDesc(User user, Pageable pageable);

    // Recent 50
    List<ListeningHistory> findTop50ByUserOrderByPlayedAtDesc(User user);

    // Clear history
    void deleteByUser(User user);
}
