package com.seniorproject.first.prototype.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seniorproject.first.prototype.token.Token;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // mb will delete
@Table(
        name = "user_table"
)
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "userId_sequence",
            sequenceName = "userId_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userId_sequence"
    )
    private Long userId;
    @Column(
            nullable = false,
            unique = true
    )
    @Email
    private String userEmail;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Length(min = 8)
    private String password;
    @Positive
    private Long age;
    private String gender;
    private String degree;

//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
//    @JsonManagedReference
    @JsonIgnore
    @OneToMany(
            mappedBy = "creator",
            fetch = FetchType.LAZY
    )
    private List<Experiment> createdExperiments;

//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
    @JsonIgnore
    @OneToMany(
            mappedBy = "participant",
            fetch = FetchType.LAZY
    )
    private List<Participation> participatedExperiments;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    @JsonIgnore
//    public List<Experiment> getCreatedExperiments() {
//        return this.createdExperiments;
//    }
//
//    @JsonIgnore
//    public void setCreatedExperiments(List<Experiment> createdExperiments) {
//        this.createdExperiments = createdExperiments;
//    }
//
//    @JsonIgnore
//    public void setParticipatedExperiments(List<Participation> participatedExperiments) {
//        this.participatedExperiments = participatedExperiments;
//    }
//
//    @JsonIgnore
//    public List<Participation> getParticipatedExperiments() {
//        return participatedExperiments;
//    }
}
