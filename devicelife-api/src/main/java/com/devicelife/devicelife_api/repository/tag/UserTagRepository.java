package com.devicelife.devicelife_api.repository.tag;

import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.UserTag;
import com.devicelife.devicelife_api.domain.user.UserTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserTagRepository extends JpaRepository<UserTag, UserTagId> {
    List<UserTag> findAllById_UserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from UserTag ut
        where ut.id.userId = :userId
          and ut.tag.tagType in :tagTypes
    """)
    void deleteByUserIdAndTagTypes(
            @Param("userId") Long userId,
            @Param("tagTypes") Set<String> tagTypes
    );

    @Query("""
        select ut.tag.tagLabel
        from UserTag ut
        where ut.user.userId = :userId
        order by ut.tag.tagLabel asc
    """)
    List<String> findTagLabelsByUserIdOrderByTagLabelAsc(
            @Param("userId") Long userId);

    void deleteAllByUser(User user);
}
