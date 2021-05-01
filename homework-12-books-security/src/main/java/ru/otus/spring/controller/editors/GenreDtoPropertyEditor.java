package ru.otus.spring.controller.editors;

import liquibase.pro.packaged.G;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.dto.GenreDto;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GenreDtoPropertyEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        GenreDto genreDto = (GenreDto) getValue();

        return genreDto == null ? "" : genreDto.getGenreId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
            return;
        }
        Pattern p = Pattern.compile(".genreId=([0-9]+).");
        Matcher matcher = p.matcher(text);
        if (!matcher.find()) {
            setValue(null);
            return;
        }
        GenreDto genreDto = GenreDto.builder()
                .genreId(Long.parseLong(matcher.group(1)))
                .build();
        setValue(genreDto);
    }
}
