package basic.shop.config;

import basic.shop.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ValidateItemInterceptor validateItemInterceptor;
    @Autowired
    private ValidateOrderInterceptor validateOrderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/*.ico", "/error",
                        "/", "/login", "/logout","/info",
                        "/member/join",
                        "/item/list", "/item/**/info/"
                );

        registry.addInterceptor(new ValidateAdminInterceptor())
                .order(2)
                .addPathPatterns("/admin/**");
//                .excludePathPatterns(
//                        "/css/**", "/*.ico", "/error",
//                        "/", "/login", "/logout",
//                        "/member/join",
//                        "/item/list", "/item/**/info/"
//                );

        registry.addInterceptor(new ValidateMemberInterceptor())
                .order(3)
                .addPathPatterns("/member/**", "/cart/**")
                .excludePathPatterns(
                        "/member/join", "/member/info",
                        "/cart/**/addCart"
                );


        registry.addInterceptor(validateItemInterceptor)
                .order(4)
                .addPathPatterns("/item/**")
                .excludePathPatterns(
                        "/item/list", "/item/**/info/", "/item/add"
                );

        registry.addInterceptor(validateOrderInterceptor)
                .order(5)
                .addPathPatterns("/order/**")
                .excludePathPatterns(
                        "/order/**/add",
                        "/order/myOrder", "/order/mySale", "/order/**/saleInfo"

                );
    }
}

