package ru.itis.reststub.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String hashPassword;

    private String confirmCode;

    private String refreshToken;

    public boolean isConfirmed() {
        return confirmCode == null || confirmCode.isBlank();
    }

}
