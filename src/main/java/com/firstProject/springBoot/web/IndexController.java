package com.firstProject.springBoot.web;

import com.firstProject.springBoot.config.auth.dto.SessionUser;
import com.firstProject.springBoot.service.posts.PostsService;
import com.firstProject.springBoot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {  //Model : 서버 템플릿 엔진에서 사용할 수있는 객체를 저장할 수 있다.
                                        //        여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달
        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        // 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성
        // 즉 로그인 성공시 httpSession.getAttribute("user")에서 값을 가져올 수 있음
        
        if(user != null) {
            // 세션에 저장된 값이 있을 때만 model에 userName으로 등록
            // 세션에 저장된값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게 됨
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() { return "posts-save"; }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {     //@PathVariable : url의 변수를 넣기 위한 선언
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
