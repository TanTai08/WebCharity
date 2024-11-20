package com.example.SGUCharity_Project.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Áp dụng Interceptor cho các route cần kiểm tra đăng nhập
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/manager/**", "/dashboard_programmanagement", "/dashboard_campaignmanagement", "/dashboard_newsmanagement",
                                 "/dashboard_servicemanagement", "/sendmessage_dashboard", "/dashboard_revenuemanagement", "/dashboard_note", "/dashboard_contact", "/dashboard_statistical")  // Chỉ áp dụng cho các route có prefix /manager
                .excludePathPatterns("/login-siteadmin");  // Exclude các route không cần kiểm tra
    }
}
