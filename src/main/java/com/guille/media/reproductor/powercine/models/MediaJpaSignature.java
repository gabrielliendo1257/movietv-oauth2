package com.guille.media.reproductor.powercine.models;

import com.guille.media.reproductor.powercine.models.listeners.SignatureListener;
import com.guille.media.reproductor.powercine.utils.enums.StatusMedia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Table(name = "media_signatures")
@Entity
@ToString
@EntityListeners(SignatureListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class MediaJpaSignature {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusMedia status;

    @Column(nullable = false, updatable = false)
    private String uploadFor;

    @Column(nullable = false, updatable = false)
    private LocalDate uploadAt;

    private LocalDate updatedAt;

    @Transient
    private String presignedUrl;

    public MediaJpaSignature(String uploadFor, String urlSignature) {
        this.uploadFor = uploadFor;
        this.presignedUrl = urlSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MediaJpaSignature that = (MediaJpaSignature) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
