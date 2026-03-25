package Auten.demo.config;

import Auten.demo.entities.Role;
import Auten.demo.entities.User;
import Auten.demo.repository.RoleRepository;
import Auten.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var adminRole = roleRepository.findByName("ADMIN");

        if (adminRole == null) {
            throw new RuntimeException("Role ADMIN não encontrada");
        }

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> System.out.println("admin já existe"),
                () -> {
                    var admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(bCryptPasswordEncoder.encode("123"));
                    admin.getRoles().add(adminRole);
                    userRepository.save(admin);

                    System.out.println("usuário admin criado com sucesso");
                }
        );
    }
}