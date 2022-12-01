package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servico")
public class ServicoControler {
	@Autowired
	private RepositorioServico repositorio;
	
	@GetMapping("/buscaServicos")
	public ResponseEntity<List<Servico>> buscarServicos(){
		List<Servico> servicos = repositorio.findAll();
		return new ResponseEntity<List<Servico>>(servicos, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Servico> buscarServico(@PathVariable Long id){
		Servico servico = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(servico == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Servico>(servico, status);
	}
}
