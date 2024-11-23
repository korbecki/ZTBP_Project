package pl.ztbd.project.oracle.entity;


import jakarta.persistence.*;
import lombok.*;
import pl.ztbd.project.security.UserDetails;

@Table(name = "USERS", schema = "FLASHCARDS_USER")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OracleUserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS-GENERATOR")
    @SequenceGenerator(name = "USERS-GENERATOR", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;
}