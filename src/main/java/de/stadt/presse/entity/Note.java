package de.stadt.presse.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "notes")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Note  {

    public Note() {
    }

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty
    private Long id;

    @NotBlank @JsonProperty
    private String title;

    @NotBlank @JsonProperty
    private String content;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate @JsonProperty
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate @JsonProperty
    private Date updatedAt;

    public Long getId() {
        return this.id;
    }

    public @NotBlank String getTitle() {
        return this.title;
    }

    public @NotBlank String getContent() {
        return this.content;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters and Setters ... (Omitted for brevity)
}
