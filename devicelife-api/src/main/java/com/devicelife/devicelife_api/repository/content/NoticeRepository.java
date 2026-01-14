package com.devicelife.devicelife_api.repository.content;

import com.devicelife.devicelife_api.domain.content.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByIsPublished(Boolean isPublished, Pageable pageable);
}
