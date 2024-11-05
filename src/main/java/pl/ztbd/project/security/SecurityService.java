package pl.ztbd.project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ztbd.project.oracle.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final JwtService jwtService;


}
