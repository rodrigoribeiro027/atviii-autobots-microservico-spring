package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Documento;

public interface RepositorioDocumento extends JpaRepository<Documento, Long>{

}
