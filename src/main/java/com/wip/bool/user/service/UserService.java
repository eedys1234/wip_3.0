package com.wip.bool.user.service;

import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.exception.excp.not_found.NotFoundDeptException;
import com.wip.bool.exception.excp.not_found.NotFoundPositionException;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.user.domain.*;
import com.wip.bool.exception.excp.not_found.NotFoundUserException;
import com.wip.bool.user.dto.OAuthAttributes;
import com.wip.bool.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private final DeptRepository deptRepository;

    private final PositionRepository positionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public Long join(UserDto.UserSaveRequest requestDto) {

        duplicationUser(requestDto.getEmail());

        //password hashing
        //db함수를 이용할 경우 dbms에 종속적이기 때문에 application에서 hashing 함수 적용
        User user = User.createUser(requestDto.getEmail(), requestDto.getName(), passwordEncoder.encode(requestDto.getUserPassword()),
                "", UserType.WIP, Role.ROLE_REQUEST);

        return userRepository.save(user).getId();
    }

    public Long update(Long userId, UserDto.UserUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundUserException(userId));

        if(!Objects.isNull(requestDto.getDeptId())) {
            Dept dept = deptRepository.findById(requestDto.getDeptId())
                    .orElseThrow(()-> new NotFoundDeptException(requestDto.getDeptId()));

            user.updateDept(dept);
        }

        if(!Objects.isNull(requestDto.getPositionId())) {
            Position position = positionRepository.findById(requestDto.getPositionId())
                    .orElseThrow(()-> new NotFoundPositionException(requestDto.getPositionId()));

            user.updatePosition(position);
        }

        return userRepository.save(user).getId();
    }

    public Long approve(Long userId) {

        User user = userRepository.findById(userId)
                .map(entity -> entity.approve())
                .orElseThrow(()-> new NotFoundUserException(userId));

        return userRepository.save(user).getId();
    }

    public Long delete(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundUserException(userId));

        return userRepository.delete(user);
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
        return userRepository.login(requestDto.getEmail(), passwordEncoder.encode(requestDto.getUserPassword()));
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

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.updateInfo(attributes.getName(), attributes.getProfiles()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .map(u -> new CustomUser(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getKey()))))
                .orElseThrow(() -> new NotFoundUserException());
    }
}

