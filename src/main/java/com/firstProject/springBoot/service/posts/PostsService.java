package com.firstProject.springBoot.service.posts;

import com.firstProject.springBoot.domain.posts.Posts;
import com.firstProject.springBoot.domain.posts.PostsRepository;

import com.firstProject.springBoot.web.dto.PostsListRespoonseDto;
import com.firstProject.springBoot.web.dto.PostsResponseDto;
import com.firstProject.springBoot.web.dto.PostsSaveRequestDto;
import com.firstProject.springBoot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        //JpaRepository에서 이미 delete메소드를 지원을 함
        // 파라미터를 삭제할 수도 있고, deleteById 메소드를 이용하면 id로 삭제할 수도 있음
        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true) // 트랜잭션 범위는 유지하되 조회 기능만 남겨두어 조회속도가 개선되기 때문에 등록 수정 삭제 기능이 전혀 없는 서비스 메소드에서 사용하는 것을 추천
    public List<PostsListRespoonseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListRespoonseDto::new) // .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }
}
