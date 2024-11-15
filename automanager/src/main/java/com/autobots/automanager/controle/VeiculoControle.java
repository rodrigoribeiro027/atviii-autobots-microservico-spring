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

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modeleos.AdicionadorLinkVeiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
	@Autowired
	private RepositorioVeiculo repositorio;
	@Autowired
	private RepositorioVenda repositorioVenda;
	@Autowired
	private AdicionadorLinkVeiculo adicionarLink;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	
	@GetMapping("/buscarVeiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos(){
		List<Veiculo> veiculos = repositorio.findAll();
		adicionarLink.adicionarLink(veiculos);
		
		if(!veiculos.isEmpty()) {
			for(Veiculo veiculo: veiculos) {
				adicionarLink.adicionarLinkUpdate(veiculo);
				adicionarLink.adicionarLinkDelete(veiculo);
			}
		}
		
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
			adicionarLink.adicionarLink(veiculo);
			adicionarLink.adicionarLinkUpdate(veiculo);
			adicionarLink.adicionarLinkDelete(veiculo);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Veiculo>(veiculo,status);
	}
	
	

	@PostMapping("/cadastro/{idUsuario}")
	public ResponseEntity<Usuario> cadastrarVeiculoCliente(@RequestBody Veiculo dados  , @PathVariable Long idUsuario){
		Usuario usuario = repositorioUsuario.findById(idUsuario).orElse(null);
		HttpStatus status = null;
		if(usuario == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			dados.setProprietario(usuario);
			usuario.getVeiculos().add(dados);
			repositorioUsuario.save(usuario);
			for(Veiculo veiculo: usuario.getVeiculos()) {
				adicionarLink.adicionarLink(veiculo);
				adicionarLink.adicionarLinkUpdate(veiculo);
				adicionarLink.adicionarLinkDelete(veiculo);
			}
			
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<Usuario>(usuario,status);
		
	}
	@DeleteMapping("/excluir/{idVeiculo}")
	public ResponseEntity<?> excluirVeiculoID(@PathVariable Long idVeiculo){
		List<Usuario> usuarios = repositorioUsuario.findAll();
		Veiculo verificacao = repositorio.findById(idVeiculo).orElse(null);
		
		if(verificacao == null) {
			return new ResponseEntity<>("Veiculo não encontrado :/",HttpStatus.NOT_FOUND);
		}
		else {
			for(Usuario usuario: repositorioUsuario.findAll()) {
				if(!usuario.getVeiculos().isEmpty()) {
					for(Veiculo veiculoUsuario: usuario.getVeiculos()) {
						if(veiculoUsuario.getId() == idVeiculo) {
							for(Usuario usuarioRegistrado: usuarios) {
								usuarioRegistrado.getVeiculos().remove(veiculoUsuario);
							}
						}
					}
				}
			}
		for(Venda venda: repositorioVenda.findAll()) {
				if(venda.getVeiculo() != null) {
					if(venda.getVeiculo().getId() == idVeiculo) {
						venda.setVeiculo(null);
					}
				}
			}
			usuarios = repositorioUsuario.findAll();
			repositorio.deleteById(idVeiculo);
			return new ResponseEntity<>("Veiculo excluido :D",HttpStatus.ACCEPTED);
		}
	}
	
	@PutMapping("/atualizar/{idVeiculo}")
	public ResponseEntity<?> atualizarVeiculoID(@PathVariable Long idVeiculo, @RequestBody Veiculo dados){
		Veiculo veiculo = repositorio.findById(idVeiculo).orElse(null);
		if(veiculo == null) {
			return new ResponseEntity<>("Veiculo não encontrado :/",HttpStatus.NOT_FOUND);
		}
		else {
			if(dados != null) {
				if(dados.getModelo() != null) {
					veiculo.setModelo(dados.getModelo());
				}
				if(dados.getPlaca() != null) {
					veiculo.setPlaca(dados.getPlaca());
				}
				repositorio.save(veiculo);
			}
			return new ResponseEntity<>(veiculo,HttpStatus.ACCEPTED);
		}
	}
}
