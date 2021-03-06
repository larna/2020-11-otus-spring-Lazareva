package ru.otus.spring.controller.editors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.controller.dto.GenreDto;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@Component
public class AuthorDtoPropertyEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        AuthorDto authorDto = (AuthorDto) getValue();

        return authorDto == null ? "" : authorDto.getAuthorId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
            return;
        }
        Pattern p = Pattern.compile(".authorId=([0-9]+).");
        Matcher matcher = p.matcher(text);
        if (!matcher.find()) {
            setValue(null);
            return;
        }
        AuthorDto authorDto = AuthorDto.builder()
                .authorId(Long.parseLong(matcher.group(1)))
                .build();
        setValue(authorDto);
    }
}
