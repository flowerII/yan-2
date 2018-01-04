package cn.qianshu.yan.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import cn.qianshu.yan.service.CustomUserService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/doc","/base","/repassword","/email_code","/book/add","/activeUser","/images/imagecode","/checkUserName","/search","/imgupload/**","/category","/registry","/book/find_by_id*").permitAll()
                .antMatchers("/department/new").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and().csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher())
                .and().formLogin().loginPage("/login") .defaultSuccessUrl("/loginindex").failureUrl("/login").permitAll()
                .and()
                .logout().permitAll().logoutSuccessUrl("/");
    }
    
    private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher(){
        CsrfSecurityRequestMatcher csrfSecurityRequestMatcher = new CsrfSecurityRequestMatcher();
        List<String> list = new ArrayList<String>();
        list.add("/testuploadimg");
        csrfSecurityRequestMatcher.setExecludeUrls(list);
        return csrfSecurityRequestMatcher;
    }
}
