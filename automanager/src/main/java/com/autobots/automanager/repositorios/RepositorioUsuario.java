package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Usuario;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long>{

}
