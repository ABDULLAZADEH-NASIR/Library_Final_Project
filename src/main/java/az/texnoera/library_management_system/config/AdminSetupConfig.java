package az.texnoera.library_management_system.config;

import az.texnoera.library_management_system.entity.Role;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.repo.RoleRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import jakarta.annotation.PostConstruct;
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
        // Admin rolunun olub-olmaması yoxlanılır
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);  // Admin rolunu əlavə edirik
        }

        // Admin istifadəçisi veritabanında yoxlanılır
        if (userRepository.findByEmail("abdullayevnasir6@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setName("Nasir");
            admin.setSurname("Abdullayev");
            admin.setFIN("5UY2TS6");
            admin.setEmail("abdullayevnasir6@gmail.com");
            admin.setPassword(passwordEncoder.encode("4145"));
            // Admin rolunu əlavə edirik
            admin.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() ->
                    new RuntimeException("No role found"))));

            // Admin istifadəçisini əlavə edirik
            userRepository.save(admin);
            System.out.println("Admin user created successfully.");
        }
    }
}