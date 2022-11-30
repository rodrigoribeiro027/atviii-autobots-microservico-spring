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
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;


@RestController
@RequestMapping("/usuario")
public class UsuarioControle {
	@Autowired
	private RepositorioUsuario repositorio;
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Usuario>> buscarUsuarios(){
		List<Usuario> usuarios = repositorio.findAll();
		return new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.FOUND);
	}
	
	@GetMapping("/buscarUsuario/{id}")
	public ResponseEntity<Usuario> buscarusuarioID(@PathVariable Long id){
		Usuario usuario = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		
		if(usuario == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Usuario>(usuario,status);
	}
	@PostMapping("/cadastro-fornecedor")
	public ResponseEntity<Usuario> cadastrarUsuarioFornecedor(@RequestBody Usuario dados){
		dados.getPerfis().add(PerfilUsuario.FORNECEDOR);
		Usuario usuario = repositorio.save(dados);
		return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
	}
}
