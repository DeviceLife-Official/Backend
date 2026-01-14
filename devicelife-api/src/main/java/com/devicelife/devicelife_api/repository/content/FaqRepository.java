package com.devicelife.devicelife_api.repository.content;

import com.devicelife.devicelife_api.domain.content.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findAllByIsPublishedOrderBySortOrderAsc(Boolean isPublished);
}
