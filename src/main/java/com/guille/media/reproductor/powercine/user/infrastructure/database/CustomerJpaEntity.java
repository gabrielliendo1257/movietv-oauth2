package com.guille.media.reproductor.powercine.user.infrastructure.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJpaEntity
{
    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 5)
    private Integer id;

    @Column(unique = true, nullable = false, updatable = false, length = 20)
    private String username;

    @Column(length = 20)
    private String lastname;

    @Column(unique = true, updatable = false, length = 50)
    private String booking;
}
