package pl.ztbd.project.cassandra.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;
import org.springframework.data.annotation.Id;
import pl.ztbd.project.security.UserDetails;

import java.util.UUID;

@Data
@AllArgsConstructor
@FieldNameConstants
@NoArgsConstructor
@Builder
@Table(value = "user")
public class UserEntity implements UserDetails {

    @Id
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, value = "email")
    private String email;

    @Column(value = "name")
    private String name;

    @Column(value = "surname")
    private String surname;

    @Column(value = "user_name")
    private String userName;

    @Column(value = "password")
    private String password;
}
