package com.seniorproject.first.prototype.model;

import com.seniorproject.first.prototype.entity.Degree;
import com.seniorproject.first.prototype.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String userEmail;
    private String firstName;
    private String lastName;
    private Long age;

    private Gender gender;
    private Degree degree;
}
