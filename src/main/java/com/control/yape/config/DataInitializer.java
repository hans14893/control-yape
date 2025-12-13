package com.control.yape.config;

import com.control.yape.model.Empresa;
import com.control.yape.model.Usuario;
import com.control.yape.repository.EmpresaRepository;
import com.control.yape.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            EmpresaRepository empresaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            System.out.println(">>> Ejecutando DataInitializer...");

            // ============================================================
            // 1) CREAR EMPRESA SISTEMA SI NO EXISTE
            // ============================================================
            Empresa empresaSistema = empresaRepository.findByNombre("EMPRESA SISTEMA")
                    .orElseGet(() -> {
                        Empresa e = new Empresa();
                        e.setNombre("EMPRESA SISTEMA");
                        e.setRuc("00000000000");
                        e.setEmailContacto("sysadmin@controlyape.com");
                        e.setActivo(true);
                        Empresa guardada = empresaRepository.save(e);
                        System.out.println("✔ Empresa SISTEMA creada con id: " + guardada.getId());
                        return guardada;
                    });

            // ============================================================
            // 2) CREAR USUARIO SUPERADMIN SI NO EXISTE
            // ============================================================
            usuarioRepository.findByUsername("superadmin")
                    .ifPresentOrElse(
                            u -> System.out.println("✔ SUPERADMIN ya existe, no se crea"),
                            () -> {
                                Usuario superAdmin = new Usuario();
                                superAdmin.setNombreCompleto("Super Administrador del Sistema");
                                superAdmin.setUsername("superadmin");
                                superAdmin.setRol("SUPERADMIN");
                                superAdmin.setActivo(true);

                                // 👉 Obligatorio porque empresa_id es NOT NULL
                                superAdmin.setEmpresa(empresaSistema);

                                // 🔐 Contraseña encriptada
                                superAdmin.setPassword(passwordEncoder.encode("super123"));

                                usuarioRepository.save(superAdmin);
                                System.out.println("✔ SUPERADMIN creado: superadmin / super123");
                            }
                    );

            System.out.println(">>> DataInitializer completado.");
        };
    }
}
