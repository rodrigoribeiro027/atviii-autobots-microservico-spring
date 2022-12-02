package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;

public interface RepositorioServico extends JpaRepository<Servico, Long>{
	Usuario save(Usuario dados);
}
