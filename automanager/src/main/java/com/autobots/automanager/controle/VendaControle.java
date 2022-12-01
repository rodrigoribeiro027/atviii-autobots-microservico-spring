package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

public class VendaControle {
	@Autowired
	public RepositorioVenda repositorio;
	
	@GetMapping("/buscarVendas")
	public ResponseEntity<List<Venda>> buscaVendas(){
		List<Venda> vendas = repositorio.findAll();
		return new ResponseEntity<List<Venda>>(vendas,HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Venda> buscarVendaID(@PathVariable Long id){
		Venda venda = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(venda == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Venda>(venda, status);
	}
}
