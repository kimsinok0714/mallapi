package com.example.mallapi.controller.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.Formatter;

// LocalDate을 문자열로 변환하거나 문자열을 LocalDate 로 변환한다.
public class LocalDateFormatter implements Formatter<LocalDate> {

    // 컨트롤러에서 반환하는 LocalDate 객체를 지정된 형식(yyyy-MM-dd)의 문자열로 변환
    // LocalDate 객체를 문자열로 변환합니다.
    @Override
    public String print(LocalDate object, Locale locale) {
        
       return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    
    }


    // 문자열을 LocalDate 로 변환한다.
    // 사용자로부터 yyyy-MM-dd 형식의 문자열 입력을 받을 경우, 이를 LocalDate 객체로 변환
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {       

        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }



}
