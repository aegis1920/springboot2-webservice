package com.bingbong.book.springboot.web;

import com.bingbong.book.springboot.service.posts.PostsService;
import com.bingbong.book.springboot.web.dto.PostsResponseDto;
import com.bingbong.book.springboot.web.dto.PostsSaveRequestDto;
import com.bingbong.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor // -> @Autowired를 안 써줘도 되는 이유
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){ // 객체형태로 전달되기 때문에 @RequestBody로 받는다.
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }


}
