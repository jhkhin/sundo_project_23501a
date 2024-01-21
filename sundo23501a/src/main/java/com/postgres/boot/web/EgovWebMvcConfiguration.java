package com.postgres.boot.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.DefaultPaginationManager;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationRenderer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.postgres.cmmn.web.EgovBindingInitializer;
import com.postgres.cmmn.web.EgovImgPaginationRenderer;
import com.postgres.sample.service.impl.jmh.AdminInterceptor;
import com.postgres.sample.service.impl.jmh.System2Interceptor;
import com.postgres.sample.service.impl.jmh.System3Interceptor;
import com.postgres.sample.service.impl.jmh.UserInfoCheckInterceptor;

@Configuration
public class EgovWebMvcConfiguration extends WebMvcConfigurationSupport {
//	WebMvcConfigurationSupport:Spring MVC의 기본 구성
//	XML이 아닌 Java Config를 통해 해당 설정할 수 있도록 해줌.

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/cmmn/validator.do").setViewName("cmmn/validator");
		registry.addViewController("/").setViewName("forward:/index.jsp");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter rmha = super.createRequestMappingHandlerAdapter();
		rmha.setWebBindingInitializer(new EgovBindingInitializer());
		return rmha;
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		Properties prop = new Properties();
		prop.setProperty("org.springframework.dao.DataAccessException", "cmmn/dataAccessFailure");
		prop.setProperty("org.springframework.transaction.TransactionException", "cmmn/transactionFailure");
		prop.setProperty("org.egovframe.rte.fdl.cmmn.exception.EgovBizException", "cmmn/egovError");
		prop.setProperty("org.springframework.security.AccessDeniedException", "cmmn/egovError");
		prop.setProperty("java.lang.Throwable", "mmn/egovError");

		Properties statusCode = new Properties();
		statusCode.setProperty("cmmn/egovError", "400");
		statusCode.setProperty("cmmn/egovError", "500");

		SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();
		smer.setDefaultErrorView("cmmn/egovError");
		smer.setExceptionMappings(prop);
		smer.setStatusCodes(statusCode);
		exceptionResolvers.add(smer);
	}

	@Bean
	public UrlBasedViewResolver urlBasedViewResolver() {
		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
		urlBasedViewResolver.setOrder(1);
		urlBasedViewResolver.setViewClass(JstlView.class);
		urlBasedViewResolver.setPrefix("/WEB-INF/views/");
		urlBasedViewResolver.setSuffix(".jsp");
		return urlBasedViewResolver;
	}

	@Bean
	public EgovImgPaginationRenderer imageRenderer() {
		return new EgovImgPaginationRenderer();
	}

	@Bean
	public DefaultPaginationManager paginationManager(EgovImgPaginationRenderer imageRenderer) {
		Map<String, PaginationRenderer> rendererType = new HashMap<>();
		rendererType.put("image", imageRenderer);
		DefaultPaginationManager defaultPaginationManager = new DefaultPaginationManager();
		defaultPaginationManager.setRendererType(rendererType);
		return defaultPaginationManager;
	}

	@Bean
	public SessionLocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("language");
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(localeChangeInterceptor());

		registry.addInterceptor(new UserInfoCheckInterceptor()) //로그인 세션 권한
				// Interceptor 적용
				.addPathPatterns("/**")

				// Interceptor 적용하지 않음
				.excludePathPatterns("/main_header", "/main_footer", "/org_intro", "/board_notice_list", "/board_notice_read"
						, "/user_login", "/user_join_write", "/user_join_agree", "/user_find_pw"
						, "/user_find_pw_new/**", "/user_find_id", "/user_find_id_result", "/user_find_pw_auth"
						, "/send_save_mail", "/write_user_info", "/id_confirm", "/user_find_pw_update", "/user_login_check"
						, "/bootstrap-5.3.2-examples/assets/dist/css/bootstrap.min.css"
						, "/bootstrap-5.3.2-examples/css/sign-in.css"
						, "/bootstrap-5.3.2-dist/css/bootstrap.css"
						, "/css/egovframework/common.css"
						, "/js/board.js"
						, "/images/**"
				)
		;

		registry.addInterceptor(new AdminInterceptor())		//	어드민 권한 조회
				.addPathPatterns("/system1/**");

		registry.addInterceptor(new System2Interceptor())	//	실시간 수문정보 관리시스템 권한 조회
				.addPathPatterns("/system2/**");

		registry.addInterceptor(new System3Interceptor())	//	수자원 시설물 관리시스템 권한 조회
				.addPathPatterns("/system3/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	@Bean
	public FilterRegistrationBean encodingFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setForceEncoding(true);
		filter.setEncoding("UTF-8");
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("*.do");
		return registrationBean;
	}

}
