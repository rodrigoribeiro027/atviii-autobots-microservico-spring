package com.autobots.automanager.controle;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;


@RestController
@RequestMapping("/usuario")
public class usuarioControle {
	@Autowired
	private RepositorioUsuario repositorio;
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Usuario>> buscarUsuarios(){
		List<Usuario> usuarios = repositorio.findAll();
		return new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.FOUND);
	}
}
