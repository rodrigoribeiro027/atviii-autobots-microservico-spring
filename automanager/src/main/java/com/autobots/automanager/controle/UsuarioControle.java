package com.autobots.automanager.controle;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;


@RestController
@RequestMapping("/usuario")
public class UsuarioControle {
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	@Autowired
	private RepositorioVenda repositorioVenda;
	@Autowired
	private RepositorioVeiculo repositorioVeiculo;
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
	public ResponseEntity<Usuario> cadastrarPerfilFornecedor(@RequestBody Usuario dados){
		dados.getPerfis().add(PerfilUsuario.FORNECEDOR);
		Usuario usuario = repositorio.save(dados);
		return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
	}
	@PostMapping("/cadastro-Cliente")
	public ResponseEntity<Usuario> cadastrarUsuarioCliente(@RequestBody Usuario dados){
		dados.getPerfis().add(PerfilUsuario.CLIENTE);
		Usuario usuario = repositorio.save(dados);
		return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/cadastro-funcionario/{idEmpresa}")
	public ResponseEntity<?> cadastrarPerfilFuncionario(@RequestBody Usuario dados, @PathVariable Long idEmpresa){
		dados.getPerfis().add(PerfilUsuario.FUNCIONARIO);
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).orElse(null);
		if(empresa == null) {
			return new ResponseEntity<String>("Empresa não encontrada :/.", HttpStatus.NOT_FOUND);
		}else {
			empresa.getUsuarios().add(dados);
			repositorioEmpresa.save(empresa);
			return new ResponseEntity<Empresa>(empresa, HttpStatus.CREATED);
		}
	}
	@DeleteMapping("/excluir/{idUsuario}")
	public ResponseEntity<?> excluirCliente(@PathVariable Long idUsuario){
		Usuario verificacao = repositorio.findById(idUsuario).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<String>("não encontrei o Usuario :/.",HttpStatus.NOT_FOUND);
		}else {
			for(Venda venda: repositorioVenda.findAll()) {
				if(venda.getCliente() != null) {		
					if(venda.getCliente().getId() == idUsuario) {
						venda.setCliente(null);
						repositorioVenda.save(venda);
					}
				}
				if(venda.getFuncionario() != null) {	
					if(venda.getFuncionario().getId() == idUsuario) {
						venda.setFuncionario(null);
						repositorioVenda.save(venda);
					}
				}
			}
			for(Veiculo veiculo: repositorioVeiculo.findAll()) {
				if(veiculo.getProprietario() != null) {	
					if(veiculo.getProprietario().getId() == idUsuario) {
						veiculo.setProprietario(null);
						repositorioVeiculo.save(veiculo);
					}
				}
			}
			for(Empresa empresa: repositorioEmpresa.findAll()) {
				if(!empresa.getUsuarios().isEmpty()) {
					for(Usuario usuario: empresa.getUsuarios()) {
						if(usuario.getId() == idUsuario) {
							empresa.getUsuarios().remove(usuario);
							repositorioEmpresa.save(empresa);
						}
						break;
					}
				}
			}
			repositorio.deleteById(idUsuario);
			return new ResponseEntity<>(repositorio.findAll(),HttpStatus.ACCEPTED);
		}
	}
	@PutMapping("/atualizar/{idUsuario}")
	public ResponseEntity<?> atualizarUsuarioID(@PathVariable Long idUsuario, @RequestBody Usuario dados){
		Usuario usuario = repositorio.findById(idUsuario).orElse(null);
		if(usuario == null) {
			return new ResponseEntity<String>("não encontrei o Usuario :/.",HttpStatus.NOT_FOUND);
		}
		else {
			if(dados != null) {
				if(dados.getNome() != null) {
					usuario.setNome(dados.getNome());
				}
				if(dados.getNomeSocial() != null) {
					usuario.setNomeSocial(dados.getNomeSocial());
				}
				repositorio.save(usuario);
			}
			return new ResponseEntity<>(usuario, HttpStatus.ACCEPTED);
		}
	}
}
