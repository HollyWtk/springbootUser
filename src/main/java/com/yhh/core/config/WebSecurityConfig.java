package com.yhh.core.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhh.core.cache.CacheService;
import com.yhh.core.security.PasswordAuthenticationFilter;
import com.yhh.core.security.SecretAuthenticationFilter;
import com.yhh.core.utils.DateTimeUtils;
import com.yhh.core.utils.ResMsg;
import com.yhh.model.entity.User;
import com.yhh.service.IUserService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private IUserService userService;
    
    @Autowired
    private RsaKeyProperties keyProperties;

    @Autowired
    CacheService tokenSvr;

    private final static String LOGIN_URL = "/yhh/login";
    
    private final static String LOGOUT_URL = "/yhh/logout";

    private static final String[] AUTH_LIST = {"/yhh/**"};

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring(). antMatchers("/swagger-ui.html")
        .antMatchers("/favicon.ico")
        .antMatchers("/doc.html")
        .antMatchers("/webjars/**")
        .antMatchers("/v2/**")
        .antMatchers("/swagger-resources/**");
    }
    
    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeRequests().antMatchers(AUTH_LIST).authenticated().and().formLogin();

        http.logout().logoutUrl(LOGOUT_URL).addLogoutHandler((request, response, authentication) -> {
        }).logoutSuccessHandler((request, response, authentication) -> {
            tokenSvr.clearToken(request.getHeader("name"));
        }).invalidateHttpSession(true).clearAuthentication(true);

        http.exceptionHandling()
        .accessDeniedHandler((request, response, accessDeniedException) -> responseText(response,
                ResMsg.ERROR(104, accessDeniedException.getMessage())))
        .authenticationEntryPoint(new AuthenticationEntryPoint() {

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException authException) throws IOException, ServletException {
                String username = request.getHeader("Name");
                if(Objects.isNull(username)) {
                    responseText(response, ResMsg.ERROR(-1, "Header Name can not be null"));
                }
                String token = request.getHeader("Authorization");
                if(Objects.isNull(token)) {
                    responseText(response, ResMsg.ERROR(-1, "Header Authorization can not be null"));
                }
                if(Objects.nonNull(token) && Objects.nonNull(username)) {
                    String exsitToken = tokenSvr.getToken(username);
                    if("deleted".equals(exsitToken)){
                        responseText(response, ResMsg.ERROR(109, "账户已被删除,请联系管理员"));
                    }else if(exsitToken != null && !exsitToken.equals(token)) {
                        responseText(response, ResMsg.ERROR(110, "账号已在别处登录，请重新登录"));
                    }else {
                        String uri = request.getRequestURI();
                        log.warn("Checked illegal request, the uri: " + uri);
                        responseText(response, ResMsg.ERROR(103, authException.getMessage()));
                    }
                }
            }
        });

        http.addFilterBefore(secretAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(passwordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);

        auth.authenticationProvider(new AuthenticationProvider() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String token = (String) authentication.getCredentials();
                if (tokenSvr.verifyToken(username, token)) {
                    return new PreAuthenticatedAuthenticationToken(username, token);
                } else {
                    log.info("User: " + username + " verify token: " + token + " failure.");
                    throw new BadCredentialsException("access deney");
                }
            }

            @Override
            public boolean supports(Class<?> arg0) {
                return arg0.equals(PreAuthenticatedAuthenticationToken.class);
            }
        });

        auth.authenticationProvider(new AuthenticationProvider() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();
                if(!tokenSvr.checkWrongTime(username)) {
                    throw new DisabledException("登陆错误次数过多,请5分钟后再尝试登陆");
                }
                UserDetails user = userDetailsService.loadUserByUsername(username);
                try {
                    boolean flag = userService.checkUserPwd(username, password);
                    if(!flag) {
                        tokenSvr.increaseWrongTime(username);
                        throw new DisabledException("账户或密码错误");
                    }
                } catch (Exception e) {
                    tokenSvr.increaseWrongTime(username);
                    throw new DisabledException("密码格式错误");
                }

                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);

            }

            @Override
            public boolean supports(Class<?> arg0) {
                return arg0.equals(UsernamePasswordAuthenticationToken.class);
            }
        });

    }

    private PasswordAuthenticationFilter passwordAuthenticationFilter() throws Exception {
        PasswordAuthenticationFilter filter = new PasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            User user = userService.getUserByMobileOrEmail(authentication.getName());
            UserResp usp = new UserResp();
            if (user != null) {
                usp.username = authentication.getName();
                usp.accountName = user.getAccountName();
                usp.mobile = user.getMobile();
                usp.roleId = user.getRoleId();
                usp.token = tokenSvr.updateToken(authentication.getName());
                usp.email = user.getEmail();
                log.info("用户:{}登陆成功,token:{}",usp.username,usp.token);
                tokenSvr.clearWrongTime(usp.username);
            }
            user.setLastLoginTime(DateTimeUtils.getStringToday());
            userService.updateById(user);
            responseText(response, ResMsg.SUCCESS_RESULT(usp));
        });
        filter.setAuthenticationFailureHandler((request, response, exception) -> responseText(response,
                ResMsg.ERROR(102, exception.getMessage())));
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl(LOGIN_URL);
        return filter;
    }

    private SecretAuthenticationFilter secretAuthenticationFilter() throws Exception {
        SecretAuthenticationFilter filter = new SecretAuthenticationFilter("/getSecretKey");
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            log.info("sucess authed:" + request.getRequestURI());
            String publicKeystr = keyProperties.getPublicKey();
            responseText(response, ResMsg.SUCCESS_RESULT(publicKeystr));
        });
        filter.setAuthenticationFailureHandler((request, response, exception) -> responseText(response,
                ResMsg.ERROR(102, exception.getMessage())));
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    private static void responseText(HttpServletResponse response, Object content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String strContent = mapper.writeValueAsString(content);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = strContent.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

    @Data
    public static class UserResp {
        String username;

        String token;

        String mobile;

        String accountName;

        Integer roleId;
        
        String email;
        
    }

}
