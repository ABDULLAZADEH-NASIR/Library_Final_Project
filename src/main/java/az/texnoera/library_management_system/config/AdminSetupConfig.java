package az.texnoera.library_management_system.config;

import az.texnoera.library_management_system.entity.Role;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.repo.RoleRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class AdminSetupConfig {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSetupConfig(UserRepo userRepository,
                            RoleRepo roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init() {
        // "ROLE_ADMIN" rolunun verilənlər bazasında olub-olmaması yoxlanılır ve eger yoxdursa yaradilir.
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);  // Admin rolunu verilənlər bazasına əlavə edirik
            System.out.println("Admin rolu uğurla yaradıldı.");
        }

        if (!userRepository.existsByEmail("abdullayevnasir6@gmail.com")) {
            User admin = new User();
            admin.setName("Nasir");
            admin.setSurname("Abdullayev");
            admin.setFIN("5UY2TS6");
            admin.setEmail("abdullayevnasir6@gmail.com");
            admin.setPassword(passwordEncoder.encode("Nasir123!"));
            admin.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() ->
                    new BasedExceptions(HttpStatus.FORBIDDEN, StatusCode.ACCESSDENIEDEXCEPTION))));
            userRepository.save(admin);
        }
    }
}