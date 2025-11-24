package com.guille.media.reproductor.powercine.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Table(name = "medias")
@Entity
@Builder
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class MediaJpaEntity {

    @Id
    private Integer id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;
    private Double popularity;

    @JsonProperty(value = "poster_path")
    private String posterPath;

    @JsonProperty(value = "release_date")
    private String releaseDate;

    @JsonProperty(value = "vote_average")
    private Double voteAverage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "media_id")
    @Builder.Default
    @JsonProperty(value = "s3_data")
    @ToString.Exclude
    private Set<MediaJpaSignature> s3Data = new HashSet<>();

    public void addMediaSignature(MediaJpaSignature signature) {
        this.s3Data.add(signature);
        signature.setMedia(this);
    }

    public void removeMediaSignature(MediaJpaSignature signature) {
        this.s3Data.remove(signature);
        signature.setMedia(null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, posterPath);
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
        MediaJpaEntity other = (MediaJpaEntity) obj;
        return Objects.equals(id, other.id) && Objects.equals(title, other.title)
                && Objects.equals(posterPath, other.posterPath);
    }

}
