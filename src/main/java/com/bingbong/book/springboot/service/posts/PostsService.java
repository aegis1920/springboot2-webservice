package com.bingbong.book.springboot.service.posts;

import com.bingbong.book.springboot.domain.posts.Posts;
import com.bingbong.book.springboot.domain.posts.PostsRepository;
import com.bingbong.book.springboot.web.dto.PostsListResponseDto;
import com.bingbong.book.springboot.web.dto.PostsResponseDto;
import com.bingbong.book.springboot.web.dto.PostsSaveRequestDto;
import com.bingbong.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor // -> Bean을 주입받을 때 `@Autowired`, `setter`, `생성자` 등의 방식이 있는데 가장 권장하는 것이 생성자 주입 방식이다. `@RequiredArgsConstructor`는 final이 선언된 모든 필드를 인자값으로 된 생성자를 만들어준다.
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true) // -> `readOnly = true`를 주면 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선된다.
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        postsRepository.delete(posts);
    }



}
