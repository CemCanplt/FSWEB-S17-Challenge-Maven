package com.workintech.spring17challenge.controller;
import com.workintech.spring17challenge.entity.*;
import com.workintech.spring17challenge.exceptions.ApiException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    List<Course> courses;
    private final LowCourseGpa lowCourseGpa;
    private final MediumCourseGpa mediumCourseGpa;
    private final HighCourseGpa highCourseGpa;

    @Autowired
    public CourseController(LowCourseGpa lowCourseGpa, MediumCourseGpa mediumCourseGpa, HighCourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @PostConstruct
    public void init() {
        courses = new ArrayList<>();
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return Collections.unmodifiableList(courses);
    }

    @GetMapping("/{name}")
    public Course getCourse(@PathVariable String name) {
        return courses.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course not found with name: " + name, HttpStatus.NOT_FOUND));
    }


    private int calculateTotalGpa(Course course) {
        int totalGpa;
        if (course.getCredit() <= 2) {
            totalGpa =  course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        } else if (course.getCredit() == 3) {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        } else if (course.getCredit() == 4) {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        } else {
            throw new ApiException("Gpa hesaplanırken bir şeyler yanlış gitti.", HttpStatus.BAD_GATEWAY);
        }
        return totalGpa;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addCourse(@RequestBody Course course) {

//        boolean exists = courses.stream().anyMatch(c -> c.getId() == (course.getId()));
//        if (exists) {
//            throw new ApiException("Course with that Id already exists: " + course.getId(), HttpStatus.BAD_REQUEST);
//        }
        if (course == null) {
            throw new ApiException("Sisteme yüklenmek istenen Course'un değeri null", HttpStatus.BAD_REQUEST);
        }

        courses.add(course);

        Map<String, Object> response = new HashMap<>();

        response.put("course", course);

        response.put("totalGpa", calculateTotalGpa(course));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable int id, @RequestBody Course course) {

        // Negatif id kontrolü
        if (id < 0) {
            throw new ApiException("Negatif id girildi.", HttpStatus.BAD_REQUEST);
        }

        // Gönderilen Course nesnesindeki id ile URL'deki id'nin uyuşup uyuşmadığını kontrol et
        if (course.getId() != id) {
            throw new ApiException("Path variable id ile Course nesnesindeki id uyuşmuyor.", HttpStatus.BAD_REQUEST);
        }

        // List'te, course içindeki id ile eşleşen kursu bul
        Course existing = null;
        for (Course c : courses) {
            if (c.getId() == id) {
                existing = c;
                break;
            }
        }

        // Eğer eşleşen bir kurs bulunamazsa hata fırlat
        if (existing == null) {
            throw new ApiException("Güncellenemedi, bu id'ye sahip bir ders bulunmuyor.", HttpStatus.BAD_REQUEST);
        }

        // Tüm alanları güncelle; isim de dahil
        existing.setName(course.getName());
        existing.setCredit(course.getCredit());
        existing.setGrade(course.getGrade());

        // Response map oluşturuluyor, güncellenen kurs ve hesaplanan toplam GPA bilgisi ile
        Map<String, Object> response = new HashMap<>();
        response.put("course", existing);
        response.put("totalGpa", calculateTotalGpa(existing));

        return ResponseEntity.ok(response);
    }




    @DeleteMapping("/{id}")
    public Course deleteCourse(@PathVariable int id) {
        // Negatif id kontrolü
        if (id < 0) {
            throw new ApiException("Negatif id girildi.", HttpStatus.BAD_REQUEST);
        }

        // List içinde, id'si parametre ile eşleşen kursu arıyoruz
        Course courseToDelete = courses.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ApiException("Bu id'ye kayıtlı bir Course zaten yok", HttpStatus.BAD_REQUEST));

        // Kurs bulunduğunda listeden kaldırıyoruz
        courses.remove(courseToDelete);

        return courseToDelete;
    }







}





























