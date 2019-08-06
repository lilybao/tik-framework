package com.jeerigger.module.config;

import com.jeerigger.common.shiro.cache.ShiroCacheManager;
import com.jeerigger.common.shiro.filter.JeeRiggerAuthenticationFilter;
import com.jeerigger.common.shiro.listener.JeeRiggerSessionListener;
import com.jeerigger.common.shiro.realm.SysUserAuthorizingRealm;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.support.shiro.JeeRiggerSessionManager;
import com.jeerigger.common.properties.JeeRiggerProperties;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class ShiroConfig implements ApplicationContextAware {

    @Autowired
    private JeeRiggerProperties properties;

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroCacheManager shiroCacheManager() {
        return new ShiroCacheManager();
    }

    private ApplicationContext applicationContext;

    /**
     * 配置会话ID生成器
     *
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     *
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
//        enterpriseCacheSessionDAO.setCacheManager(shiroCacheManager());
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("jeerigger.session.id");
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置session监听
     *
     * @return
     */
    @Bean("sessionListener")
    public JeeRiggerSessionListener sessionListener() {
        JeeRiggerSessionListener sessionListener = new JeeRiggerSessionListener();
        return sessionListener;
    }

    /**
     * 配置会话管理器，设定会话超时及保存
     *
     * @return
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        JeeRiggerSessionManager sessionManager = new JeeRiggerSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(simpleCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(shiroCacheManager());
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    @Bean
    public AuthorizingRealm shiroAuthorizingRealm() {
        AuthorizingRealm realm;
        if (properties.getShiro().getRealm() != null) {
            realm = applicationContext.getBean(properties.getShiro().getRealm());
        } else {
            realm = applicationContext.getBean(SysUserAuthorizingRealm.class);
        }
//        if (realm != null) {
//            realm.setCacheManager(shiroCacheManager());
//            realm.setAuthorizationCacheName("authorization_cache");
//            realm.setAuthenticationCacheName("authentication_cache");
//        } else {
//            throw new FrameException("error AuthorizingRealm!!!");
//        }
        return realm;
    }


    /**
     * 配置核心安全事务管理器
     *
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置自定义realm.
        securityManager.setRealm(shiroAuthorizingRealm());
        //配置记住我
        securityManager.setRememberMeManager(cookieRememberMeManager());
        //配置 缓存管理器
        securityManager.setCacheManager(shiroCacheManager());
        //配置自定义session管理，使用ehcache 或redis
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(30 * 24 * 60 * 60);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * 生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("B9rPF8FHhxKJZ9k63ik7kQ=="));

        return cookieRememberMeManager;
    }

    /**
     * 配置MD5密码比较器
     * 校验规则HashedCredentialsMatcher
     * 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        // 1.创建ShiroFilterFactoryBean对象
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = shiroFilter.getFilters();
        filterMap.put("authc", new JeeRiggerAuthenticationFilter());

        // 2.实现filterChainDefinitions
        // 2.1.创建LinkedHashMap
        Map<String, String> filterChainMap = new LinkedHashMap<String, String>();

        filterChainMap.put("/swagger-ui.html", "anon");
        filterChainMap.put("/webjars/**", "anon");
        filterChainMap.put("/v2/**", "anon");
        filterChainMap.put("/swagger-resources/**", "anon");

        filterChainMap.put("/resources/**", "anon");
        filterChainMap.put("/static/**", "anon");
        filterChainMap.put("/index.html", "anon");

        filterChainMap.put("/login", "anon");
        filterChainMap.put("/login_error", "anon");
        filterChainMap.put("/captchaCode", "anon");
        filterChainMap.put("/activiti/**", "anon");
        filterChainMap.put("/model-list.html","anon");

        if (properties.getShiro().getAnonUrl() != null) {
            for (String strUrl : properties.getShiro().getAnonUrl()) {
                if (strUrl.indexOf("**") > 0 || strUrl.indexOf("/**") > 0) {
                    continue;
                }
                filterChainMap.put(strUrl, "anon");
            }
        }
        filterChainMap.put("/logout", "logout");
        filterChainMap.put("/**", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterChainMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
