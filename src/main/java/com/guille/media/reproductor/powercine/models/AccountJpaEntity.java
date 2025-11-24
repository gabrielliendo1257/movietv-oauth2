package com.guille.media.reproductor.powercine.models;

import com.guille.media.reproductor.powercine.utils.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Table(name = "accounts")
@Entity
@Builder
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class AccountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 300)
    private String password;

    @Column(length = 100)
    private String email;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    private Roles rol;

    @Override
    public int hashCode() {
        return Objects.hash(id, username, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AccountJpaEntity other = (AccountJpaEntity) obj;
        return Objects.equals(id, other.id) && Objects.equals(username, other.username)
                && Objects.equals(createdAt, other.createdAt);
    }
}
