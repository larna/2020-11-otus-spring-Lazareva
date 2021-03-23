package ru.otus.spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import ru.otus.spring.controller.convertors.author.DtoToAuthorConverter;
import ru.otus.spring.controller.convertors.book.DtoToBookConverter;
import ru.otus.spring.controller.convertors.genre.DtoToGenreConverter;
import ru.otus.spring.controller.convertors.comment.CommentToDtoConverter;
import ru.otus.spring.controller.convertors.author.AuthorToDtoConverter;
import ru.otus.spring.controller.convertors.book.BookToDtoConverter;
import ru.otus.spring.controller.convertors.genre.GenreToDtoConverter;

import java.util.Locale;

/**
 * Настройка локализации
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public MessageSource messageSource() {
        var ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:/i18n/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.forLanguageTag("ru"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DtoToAuthorConverter authorDtoToDomainConverter = new DtoToAuthorConverter();
        DtoToGenreConverter genreDtoToDomainConverter = new DtoToGenreConverter();
        registry.addConverter(authorDtoToDomainConverter);
        registry.addConverter(genreDtoToDomainConverter);
        registry.addConverter(new DtoToBookConverter(genreDtoToDomainConverter, authorDtoToDomainConverter));

        AuthorToDtoConverter domainToAuthorDtoConverter = new AuthorToDtoConverter();
        GenreToDtoConverter domainToGenreDtoConverter = new GenreToDtoConverter();
        registry.addConverter(domainToAuthorDtoConverter);
        registry.addConverter(domainToGenreDtoConverter);
        registry.addConverter(new BookToDtoConverter(domainToAuthorDtoConverter, domainToGenreDtoConverter));

        registry.addConverter(new CommentToDtoConverter());
    }
}
