package com.control.yape.config;

import com.control.yape.model.Empresa;
import com.control.yape.model.Usuario;
import com.control.yape.repository.EmpresaRepository;
import com.control.yape.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("prod")
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            EmpresaRepository empresaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (usuarioRepository.count() > 0) {
                System.out.println(">>> Usuarios ya existen, DataInitializer no se ejecuta");
                return;
            }

            System.out.println(">>> Ejecutando DataInitializer...");

            Empresa empresaSistema = empresaRepository.findByNombre("EMPRESA SISTEMA")
                    .orElseGet(() -> {
                        Empresa e = new Empresa();
                        e.setNombre("EMPRESA SISTEMA");
                        e.setRuc("00000000000");
                        e.setEmailContacto("sysadmin@controlyape.com");
                        e.setActivo(true);
                        return empresaRepository.save(e);
                    });

            Usuario superAdmin = new Usuario();
            superAdmin.setNombreCompleto("Super Administrador del Sistema");
            superAdmin.setUsername("superadmin");
            superAdmin.setRol("SUPERADMIN");
            superAdmin.setActivo(true);
            superAdmin.setEmpresa(empresaSistema);
            superAdmin.setPassword(passwordEncoder.encode("super123"));

            usuarioRepository.save(superAdmin);

            System.out.println("✔ SUPERADMIN creado: superadmin / super123");
        };
    }


}
