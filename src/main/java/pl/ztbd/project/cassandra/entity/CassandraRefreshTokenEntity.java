package pl.ztbd.project.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import pl.ztbd.project.cassandra.entity.key.RefreshTokenKey;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@FieldNameConstants
@NoArgsConstructor
@Builder
@Table(value = "refresh_token")
public class CassandraRefreshTokenEntity {

    @PrimaryKey
    private RefreshTokenKey refreshTokenKey;

    @Column(value = "refresh_token")
    private String refreshToken;

    @Column(value = "refresh_token_expiration")
    private OffsetDateTime refreshTokenExpiration;
}
