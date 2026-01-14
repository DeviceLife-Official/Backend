package com.devicelife.devicelife_api.repository.content;

import com.devicelife.devicelife_api.domain.content.Inquiry;
import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAllByUserId(Long userId, Pageable pageable);

    Page<Inquiry> findAllByUserIdAndStatus(Long userId, InquiryStatus status, Pageable pageable);

    Page<Inquiry> findAllByStatus(InquiryStatus status, Pageable pageable);

    Page<Inquiry> findAllByStatusIn(List<InquiryStatus> statuses, Pageable pageable);
}
