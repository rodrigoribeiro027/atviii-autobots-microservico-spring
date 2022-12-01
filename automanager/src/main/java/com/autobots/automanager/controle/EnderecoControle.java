package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.repositorios.RepositorioEndereco;

public class EnderecoControle {
	@Autowired
	private RepositorioEndereco repositorio;
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Endereco>> buscarEnderecos(){
		List<Endereco> enderecos = repositorio.findAll();
		return new ResponseEntity<List<Endereco>>(enderecos,HttpStatus.FOUND);
	}
	
	@GetMapping("/busca/{id}")
	public ResponseEntity<Endereco> buscarEnderecoID(@PathVariable
			Long id){
		Endereco endereco = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(endereco == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Endereco>(endereco,status);
	}
}
