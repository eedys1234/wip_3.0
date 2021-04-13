package com.wip.bool.service.user;

import com.wip.bool.domain.cmmn.encrypt.PBKDF2;
import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.dept.DeptRepository;
import com.wip.bool.domain.position.Position;
import com.wip.bool.domain.position.PositionRepository;
import com.wip.bool.domain.user.Role;
import com.wip.bool.domain.user.User;
import com.wip.bool.domain.user.UserRepository;
import com.wip.bool.web.dto.auth.OAuthAttributes;
import com.wip.bool.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private final DeptRepository deptRepository;

    private final PositionRepository positionRepository;

    public Long join(UserDto.UserSaveRequest requestDto) {

        duplicationUser(requestDto.getEmail());

        //password hashing
        //db함수를 이용할 경우 dbms에 종속적이기 때문에 application에서 hashing 함수 적용
        String hashingPassword = new PBKDF2(requestDto.getUserPassword()).hash();
        User user = User.createUser(requestDto.getEmail(), requestDto.getName(), hashingPassword, "", Role.REQUEST);

        return userRepository.save(user).getId();
    }

    public Long update(Long userId, UserDto.UserUpdateRequest requestDto) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        if(!Objects.isNull(requestDto.getDeptId())) {
            Dept dept = deptRepository.findById(requestDto.getDeptId())
                    .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + requestDto.getDeptId()));

            user.updateDept(dept);
        }

        if(!Objects.isNull(requestDto.getPositionId())) {
            Position position = positionRepository.findById(requestDto.getPositionId())
                    .orElseThrow(()-> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + requestDto.getPositionId()));

            user.updatePosition(position);
        }

        return userRepository.save(user).getId();
    }

    public Long approve(Long userId) {

        User user = userRepository.findById(userId)
                .map(entity -> entity.approve())
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        return userRepository.save(user).getId();
    }

    public Long delete(Long userId) {
        return userRepository.delete(userId);
    }

    @Transactional(readOnly = true)
    public List<UserDto.UserResponse> findAllByRole(Role role) {
        return userRepository.findAllByRole(role).stream()
                .map(UserDto.UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto.UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto.UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long login(UserDto.UserLoginRequest requestDto) {

        String hashingPassword = new PBKDF2(requestDto.getUserPassword()).hash();
        return userRepository.login(requestDto.getEmail(), hashingPassword);
    }

    private void duplicationUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("email이 존재하지 않습니다. email = " + email));

        if(!Objects.isNull(user)) {
            throw new IllegalStateException("");
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                                                    .getProviderDetails()
                                                    .getUserInfoEndpoint()
                                                    .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());


        User user = saveOrUpdate(attributes);

        return null;
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.updateInfo(attributes.getName(), attributes.getProfiles()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

}

