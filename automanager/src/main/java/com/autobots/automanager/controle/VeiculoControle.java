package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
	@Autowired
	private RepositorioVeiculo repositorio;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@GetMapping("/buscarVeiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos(){
		List<Veiculo> veiculos = repositorio.findAll();
		return new ResponseEntity<List<Veiculo>>(veiculos,HttpStatus.FOUND);
	}
	@GetMapping("/obterVeiculo/{id}")
	public ResponseEntity<Veiculo> buscarVeiculo(@PathVariable Long id){
		Veiculo veiculo = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(veiculo == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Veiculo>(veiculo,status);
	}
	@PostMapping("/cadastrar/{idUsuario}")
	public ResponseEntity<Usuario> cadastrarVeiculoCliente(@RequestBody Veiculo dados, @PathVariable Long idUsuario){
		Usuario usuario = repositorioUsuario.findById(idUsuario).orElse(null);
		HttpStatus status = null;
		if(usuario == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			dados.setProprietario(usuario);
			usuario.getVeiculos().add(dados);
			repositorioUsuario.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<Usuario>(usuario,status);
	}
}
