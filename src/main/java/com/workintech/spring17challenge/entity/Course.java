package com.workintech.spring17challenge.entity;

import com.workintech.spring17challenge.exceptions.ApiException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @NotNull(message = "ID boş olamaz.")
    private Integer id;

//    @NotNull(message = "Ders adı boş olamaz.")
    private String name;

//    @NotNull(message = "Kredi boş olamaz.")
    private Integer credit;

//    @NotNull(message = "Not bilgisi boş olamaz.")
    private Grade grade;

    public void setCredit(int x) {
        if (x > 4 || 0 >= x) {
            throw new ApiException("Credit değeri 1-4 arası olmalıdır.", HttpStatus.BAD_REQUEST);
        }

        this.credit = x;
    }

}
