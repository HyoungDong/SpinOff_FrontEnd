package com.nameless.spin_off.service.query;

import com.nameless.spin_off.dto.CollectionDto;
import com.nameless.spin_off.dto.CollectionDto.MainPageCollectionDto;
import com.nameless.spin_off.dto.PostDto;
import com.nameless.spin_off.dto.PostDto.MainPagePostDto;
import com.nameless.spin_off.entity.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface MainPageService {
    Slice<MainPagePostDto> getPostsOrderById(Pageable pageable);
    Slice<MainPagePostDto> getPostsOrderByPopularityBySlicingAfterLocalDateTime(
            Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime);
    Slice<MainPageCollectionDto> getCollectionsOrderByPopularityBySlicingAfterLocalDateTime(
            Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
