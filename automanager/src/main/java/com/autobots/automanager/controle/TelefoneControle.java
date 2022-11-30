package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.repositorios.RepositorioTelefone;

public class TelefoneControle {
	
	@Autowired
	private RepositorioTelefone repositorio;
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Telefone>> buscarTelefones(){
		List<Telefone> telefones = repositorio.findAll();
		return new ResponseEntity<List<Telefone>>(telefones,HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Telefone> buscarTelefone(@PathVariable Long id){
		Telefone telefone = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(telefone == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Telefone>(telefone,status);
	}
}
