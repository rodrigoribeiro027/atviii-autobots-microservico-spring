package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

public class MercadoriaControle {
	@Autowired
	private RepositorioMercadoria repositorio;
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Mercadoria>> buscarMercadorias(){
		List<Mercadoria> mercadorias = repositorio.findAll();
		return new ResponseEntity<List<Mercadoria>>(mercadorias, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Mercadoria> buscarMercadoriaID(@PathVariable Long id){
		Mercadoria mercadoria = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(mercadoria == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Mercadoria>(mercadoria,status);
	}
}
