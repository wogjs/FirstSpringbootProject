package com.firstProject.springBoot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)                     //스프링 부트 테스트와 Junit사이에 연결자 역할
@WebMvcTest(controllers = HelloController.class) // 여러 스프링 테스트 어노테이션중 Web에 집중할 수있는 어노테이션
                                                 // 선언할 경우 @Controller, @ControllerAdvice 등을 사용
                                                 // @Service, @Component, @Repository 등을 사용x
public class HelloControllerTest {
    @Autowired                                   // 스프링이 관리하는 빈을 주입 받는다.
    private MockMvc mvc;                         // 웹 API를 테스트할 때 사용 스프링 MVC 테스트의 시작점

    @Test
    public  void hello가_리턴된다() throws  Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))     // MockMvc를 통해 /hello 주소로 HTTP GET 요청을 합니다.
                .andExpect(status().isOk())      // mvc.perform의 결과를 검증합니다. HTTP Header의 Status를 검증
                .andExpect(content().string(hello)); //mvc.perform의 결과를 검증 응답본문의 내용을 검증 hello를 리턴하기 때문에 이값이 맞는 검증
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)                                        // param : API 테스트할 때 사용될 요청 파라미터를 설정 단 값은 String만 허용
                        .param("amount", String.
                                valueOf(amount)))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.name", is(name)))     
                                    .andExpect(jsonPath("$.amount", is(amount)));
                                    // jsonPath : Json 응답값을 필드별로 검증할 수 있는 메소드
                                    //            $를 기준으로 필드명을 명시
                                    //            여기서는 name과 amount를 검증하니 $.name, $.amount로 검증
    }
}
