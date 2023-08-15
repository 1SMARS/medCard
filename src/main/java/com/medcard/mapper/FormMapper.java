//package com.medcard.mapper;
//
//import com.medcard.dto.FormDto;
//import com.medcard.entities.Form;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@Component
//public class FormMapper implements Function<Form, FormDto> {
//    @Override
//    public FormDto apply(Form form) {
//        return new FormDto(
//                form.getDisease(),
//                form.getDescription()
//        );
//    }
//}
