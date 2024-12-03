package pl.ztbd.project.oracle.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Table(name = "REFRESH_TOKEN", schema = "FLASHCARDS_USER")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OracleRefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFRESH_TOKEN-GENERATOR")
    @SequenceGenerator(name = "REFRESH_TOKEN-GENERATOR", sequenceName = "refresh_token_seq", allocationSize = 1)
    @Column(name = "REFRESH_TOKEN_ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(name = "REFRESH_TOKEN_EXPIRATION")
    public OffsetDateTime refreshTokenExpiration;

    public OracleRefreshTokenEntity(Long userId, OffsetDateTime refreshTokenExpiration, String refreshToken) {
        this.userId = userId;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.refreshToken = refreshToken;
    }

}