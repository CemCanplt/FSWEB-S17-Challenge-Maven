package com.workintech.spring17challenge.model;

import org.springframework.stereotype.Component;

@Component
public class MediumCourseGpa implements CourseGpa {
    @Override
    public int grtGpa() {
        return 5;
    }
}
