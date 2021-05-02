//package com.wip.bool.jwt;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Value("spring.jwt.accesstoken")
//    private String accessToken;
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    //API 호출은 JWT를 확인한다.
//    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/v1/**");
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        //permit All 또한 필터를 통과한다. 즉, 필터는 항상 통과한다.
//        if(requestMatcher.matches(request)) {
//
//            Cookies cookies = new Cookies();
//            String jwt = null;
//            try {
//
//                jwt = cookies.getCookieValue(request, accessToken);
//
//            } catch(NullPointerException exception) {
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            }
//
//            //cookies 값이 안들어왔을 경우
//            if(StringUtils.isEmpty(jwt)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            }
//            else {
//
//                //jwt가 유효하지 않을경우
//                if(!jwtTokenProvider.isValidateToken(jwt)) {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                }
//
//                //jwt가 만료되었다면
//                if(!jwtTokenProvider.isExpireToken(jwt)) {
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                }
//
//                Map<String, Object> attributes = jwtTokenProvider.getBodyFromToken(jwt);
//
//                String userNameAttributeName = "sub";
//
//                List<GrantedAuthority> authorities = (List<GrantedAuthority>) attributes.get("roles");
//
////                List<GrantedAuthority> authorities = new ArrayList<>();
////                authorities.add(new SimpleGrantedAuthority(Role.NOMARL.getKey()));
//
//                OAuth2User userDetail = new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
//                OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(userDetail, authorities,
//                        userNameAttributeName);
//
//                authenticationToken.setDetails(userDetail);
//
//            }
//
//        }
//
//    }
//}
