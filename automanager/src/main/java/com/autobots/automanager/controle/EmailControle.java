package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/email")
public class EmailControle {

	@Autowired
	private RepositorioEmail repositorio;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@GetMapping("/buscarEmails")
	public ResponseEntity<List<Email>> buscarEmails(){
		List<Email> emails = repositorio.findAll();
		return new ResponseEntity<List<Email>>(emails,HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Email> buscarEmailID(@PathVariable Long id){
		Email email = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(email == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Email>(email,status);
	}
	
	@DeleteMapping("/excluir/{idEmail}")
	public ResponseEntity<?> excluirEmail(@PathVariable Long idEmail){
		Email verificacao = repositorio.findById(idEmail).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<>("Email não econtrado", HttpStatus.NOT_FOUND);
		}
		else {
			for(Usuario usuario: repositorioUsuario.findAll()) {
				if(!usuario.getEmails().isEmpty()) {
					
					for(Email email: usuario.getEmails()) {
						if(email.getId() == idEmail) {
							usuario.getEmails().remove(email);
							repositorioUsuario.save(usuario);
						}
					}
					
				}
			}
			return new ResponseEntity<>("Email excluido.", HttpStatus.ACCEPTED);
		}
	}
	@PutMapping("/atualizar/{idEmail}")
	public ResponseEntity<?> atualizarEmail(@PathVariable Long idEmail, @RequestBody Email dados){
		Email email = repositorio.findById(idEmail).orElse(null);
		if(email == null) {
			return new ResponseEntity<>("Email não econtrado", HttpStatus.NOT_FOUND);
		}
		else {
			if(dados != null) {
				if(dados.getEndereco() != null) {
					email.setEndereco(dados.getEndereco());
				}
				repositorio.save(email);
			}
			return new ResponseEntity<>(email, HttpStatus.ACCEPTED);
		}
	}
	

}
