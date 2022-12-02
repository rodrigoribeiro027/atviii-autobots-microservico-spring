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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioUsuario;

public class EnderecoControle {
	@Autowired
	private RepositorioEndereco repositorio;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	
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
	@DeleteMapping("/excluir/{idEndereco}")
	public ResponseEntity<?> excluirEnderecoID(@PathVariable Long idEndereco){
		Endereco verificacao = repositorio.findById(idEndereco).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<>("Endereco não econtrado...", HttpStatus.NOT_FOUND);
		}
		else {
			List<Usuario> usuarios = repositorioUsuario.findAll(); 
			for(Usuario usuario: usuarios) {
				if(usuario.getEndereco() != null) {
					if(usuario.getEndereco().getId() == idEndereco) {
						usuario.setEndereco(null);
						repositorioUsuario.save(usuario);
						break;
					}
				}
			}
			for(Empresa empresa: repositorioEmpresa.findAll()) {
				if(empresa.getEndereco() != null) {
					if(empresa.getEndereco().getId() == idEndereco) {
						empresa.setEndereco(null);
						repositorioEmpresa.save(empresa);
						break;
					}
				}
			}
			return new ResponseEntity<>("Endereco excluido :D.", HttpStatus.ACCEPTED);
		}
	}

	
	@PutMapping("/atualizar/{idEndereco}")
	public ResponseEntity<?> AtualizarEnderecoID(@PathVariable Long idEndereco, @RequestBody Endereco dados){
		Endereco endereco = repositorio.findById(idEndereco).orElse(null);
		if(endereco == null) {
			return new ResponseEntity<>("Endereco não econtrado :/", HttpStatus.NOT_FOUND);
		}else {
			if(dados != null) {
				if(dados.getEstado() != null) {
					endereco.setEstado(dados.getEstado());
				}
				if(dados.getCidade() != null) {
					endereco.setCidade(dados.getCidade());
				}
				if(dados.getBairro() != null) {
					endereco.setBairro(dados.getBairro());
				}
				if(dados.getRua() != null) {
					endereco.setRua(dados.getRua());
				}
				if(dados.getNumero() != null) {
					endereco.setNumero(dados.getNumero());
				}
				if(dados.getCodigoPostal() != null) {
					endereco.setCodigoPostal(dados.getCodigoPostal());
				}
				if(dados.getInformacoesAdicionais() != null) {
					endereco.setInformacoesAdicionais(dados.getInformacoesAdicionais());
				}
				repositorio.save(endereco);
			}
			return new ResponseEntity<>(endereco, HttpStatus.ACCEPTED);
		}
	}
	
}
