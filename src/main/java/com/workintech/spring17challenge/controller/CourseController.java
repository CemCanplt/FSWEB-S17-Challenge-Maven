package com.workintech.spring17challenge.controller;
import com.workintech.spring17challenge.model.Course;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

    List<Course> courses;

    public CourseController() {
        courses = new ArrayList<>();
    }


}
