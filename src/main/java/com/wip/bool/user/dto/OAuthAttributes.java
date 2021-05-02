package com.wip.bool.user.dto;

import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String profiles;
    private String registrationId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name,
                           String email, String profiles, String registrationId) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.profiles = profiles;
        this.registrationId = registrationId;

    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver("id", registrationId, attributes);
        }

        return ofGoogle(userNameAttributeName, registrationId, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, String registrationId, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profiles(String.valueOf(attributes.get("picture")))
                .registrationId(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, String registrationId, Map<String, Object> attributes) {

        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return OAuthAttributes.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .profiles(String.valueOf(response.get("profile_image")))
                .registrationId(registrationId)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.createUser(this.email, this.name, this.profiles, UserType.valueOf(registrationId.toUpperCase()), Role.ROLE_REQUEST);
    }

}
