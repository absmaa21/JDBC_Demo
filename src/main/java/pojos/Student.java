package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class Student {
    // For database objects we use Wrapper-Classes
    private Integer studentId;
    @ToString.Exclude
    private Integer catNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Float size;

    private Float avgGrade;


}
