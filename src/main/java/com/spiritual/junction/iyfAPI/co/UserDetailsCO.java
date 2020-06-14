package com.spiritual.junction.iyfAPI.co;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserDetailsCO {

    @NotNull(message = "Email cannot be null")
    String email;
    String  firstName;
    String  lastName;
    String  username;
    Long    phoneNumber;
    Long    mobile;
    String  profession;
    String  gender;
    String  collegeName;
    Integer age;
    String  areaOfResidence;

}
