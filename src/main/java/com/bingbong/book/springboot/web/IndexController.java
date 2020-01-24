package com.bingbong.book.springboot.web;

import com.bingbong.book.springboot.config.auth.LoginUser;
import com.bingbong.book.springboot.config.auth.dto.SessionUser;
import com.bingbong.book.springboot.domain.user.User;
import com.bingbong.book.springboot.service.posts.PostsService;
import com.bingbong.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    // 항상 썼던 SessionUser user = (SessionUser) httpSession.getAttribute("user"); 코드가 @LoginUser로 대체되어 이 어노테이션만으로 세션 정보를 가져올 수 있게됨
    public String index(Model model, @LoginUser SessionUser user){
        model.addAttribute("posts", postsService.findAllDesc());

        if(user != null){
            model.addAttribute("originUserName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
