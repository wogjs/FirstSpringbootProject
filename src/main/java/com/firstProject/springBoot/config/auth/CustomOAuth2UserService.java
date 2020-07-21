package com.firstProject.springBoot.config.auth;

import com.firstProject.springBoot.config.auth.dto.OAuthAttributes;
import com.firstProject.springBoot.config.auth.dto.SessionUser;
import com.firstProject.springBoot.domain.user.User;
import com.firstProject.springBoot.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.
                getClientRegistration().getRegistrationId();    // 현재 로그인 중인 서비스를 구분하는 코드 //
                                                                // 지금은 구글만 사용하는 불필요한 값, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        //userNameAttributeName OAuth2 로그인 진행 시 키가 되는 필드값을 이야기함, primary key와 같은 의미
        // 구글의 경우 기본으로 코드를 지원하지만, 네이버 카카오 등은 기본지원하지 않음, 구글의 기본코드는 sub
        // 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        //OAuthAttributes : OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스입니다.
        // 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
        // 바로아래에서 이 클래스의 코드가 나오니 차례로 생성하면 됨

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        //SessionUser 세션에 사용자 정보를 저장하기 위한 DTO클래스
        //User클래스에 직렬화를 구현하지 않음
        // 엔티티클래스에는 언제 다른 엔티티와 관계가 형성될지 모름
        // 직렬화 기능을 가진 세션 DTO를 하나 추가로 만드는 것이 이후 운영 및 유지보수 때 많은 도움이 됨

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
