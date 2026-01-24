package edu.springboot.organizer.web.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class Internationalization implements WebMvcConfigurer {

    @Value("${locale.resolver.type}")
    private String resolverType;

    /**
     * <a href="https://stackoverflow.com/a/73167624/31086313">Not able to load a font in thymeleaf</a>
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/fonts/**")
                .addResourceLocations("classpath:/static/fonts/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(30)))
                .resourceChain(true);

    }

    @Bean
    public LocaleResolver localeResolver() {
        LocaleResolver localeResolver;
        if ("COOKIE".equals(resolverType)) {
            localeResolver = new CookieLocaleResolver();
            ((CookieLocaleResolver) localeResolver).setDefaultLocale(Locale.ENGLISH);
        } else {
            localeResolver = new SessionLocaleResolver();
            ((SessionLocaleResolver) localeResolver).setDefaultLocale(Locale.ENGLISH);
        }
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor
                = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource
                = new ResourceBundleMessageSource();
        messageSource.setBasename("lang/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void
    addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
