package com.autobots.automanager.controle;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modeleos.VendaMolde;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

public class CredencialControle {
	
	@Autowired
	public RepositorioMercadoria repositorioMercadoria;
	@Autowired
	public RepositorioServico repositorioServico;
	@Autowired
	public RepositorioVeiculo repositorioVeiculo;
	@Autowired
	public RepositorioVenda repositorio;
	@Autowired
	public RepositorioEmpresa repositorioEmpresa;
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	@GetMapping("/buscar")
	public ResponseEntity<List<Venda>> buscarVendas() {
		List<Venda> vendas = repositorio.findAll();
		return new ResponseEntity<List<Venda>>(vendas, HttpStatus.FOUND);
	}

	@GetMapping("/buscar/{id}")
	public ResponseEntity<Venda> buscaVendaID(@PathVariable Long id) {
		Venda venda = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if (venda == null) {
			status = HttpStatus.NOT_FOUND;
		} 
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Venda>(venda, status);
	}
	@PostMapping("/cadastrar")
	public ResponseEntity<Empresa> cadastrarVenda(@RequestBody VendaMolde dados){
		Empresa empresa = repositorioEmpresa.findById(dados.getIdEmpresa()).orElse(null);
		Venda venda = new Venda();
		if(empresa == null) {
			return new ResponseEntity<Empresa>(empresa,HttpStatus.NOT_FOUND);
		}else {
			venda.setCliente(repositorioUsuario.findById(dados.getIdCliente()).orElse(null));
			venda.setFuncionario(repositorioUsuario.findById(dados.getIdFuncionario()).orElse(null));
			venda.setVeiculo(repositorioVeiculo.findById(dados.getIdVeiculo()).orElse(null));
			venda.setCadastro(new Date());
			venda.setIdentificacao(dados.getIdentificacao());
			repositorio.save(venda);
			
			Set<Long> idsMercadorias = dados.getIdMercadorias();
			Set<Long> idsServicos = dados.getIdServicos();
			
			if(dados.getIdMercadorias() != null) {
				if(dados.getIdMercadorias().size() > 0) {	
					for (Long id : idsMercadorias) {
						Mercadoria respostaBuscar = repositorioMercadoria.getById(id);
						Mercadoria mercadoria = new Mercadoria();
						mercadoria.setValidade(respostaBuscar.getValidade());
						mercadoria.setFabricao(respostaBuscar.getFabricao());
						mercadoria.setCadastro(respostaBuscar.getCadastro());
						mercadoria.setNome(respostaBuscar.getNome());
						mercadoria.setQuantidade(respostaBuscar.getQuantidade());
						mercadoria.setValor(respostaBuscar.getValor());
						mercadoria.setDescricao(respostaBuscar.getDescricao());
						mercadoria.setOriginal(false);
						venda.getMercadorias().add(mercadoria);
					}
				}
			}

			if(dados.getIdMercadorias() != null) {	
				if(dados.getIdServicos().size() > 0) {				
					for (Long id : idsServicos) {
						Servico respostaBusca = repositorioServico.getById(id);
						Servico servico = new Servico();
						servico.setNome(respostaBusca.getNome());
						servico.setDescricao(respostaBusca.getDescricao());
						servico.setValor(respostaBusca.getValor());
						servico.setOriginal(false);
						venda.getServicos().add(servico);
					}
				}
			}
			
			repositorio.save(venda);
			
			empresa.getVendas().add(venda);
			Usuario funcionario = venda.getFuncionario();
			
			funcionario.getVendas().add(venda);
			repositorioEmpresa.save(empresa);
			
			return new ResponseEntity<Empresa>(empresa,HttpStatus.CREATED);
		}
	}
	@DeleteMapping("/excluir/{idVenda}")
	public ResponseEntity<?> excluirVendaID(@PathVariable Long idVenda) {
		List<Empresa> empresas = repositorioEmpresa.findAll();
		List<Usuario> usuarios = repositorioUsuario.findAll();
		List<Veiculo> veiculos = repositorioVeiculo.findAll();
		Venda verificador = repositorio.findById(idVenda).orElse(null);

		if (verificador == null) {
			return new ResponseEntity<>("Venda não encontrada :/", HttpStatus.NOT_FOUND);
		} 
		else {
			for (Empresa empresa : repositorioEmpresa.findAll()) {
				if (!empresa.getVendas().isEmpty()) {
					for (Venda vendaEmpresa : empresa.getVendas()) {
						if (vendaEmpresa.getId() == idVenda) {
							for (Empresa empresaRegistrada : empresas) {
								empresaRegistrada.getVendas().remove(vendaEmpresa);
							}
						}
					}
				}
			}
			for (Usuario usuario : repositorioUsuario.findAll()) {
				if (!usuario.getVendas().isEmpty()) {
					for (Venda vendaUsuario : usuario.getVendas()) {
						if (vendaUsuario.getId() == idVenda) {
							for (Usuario usuarioRegistrado : usuarios) {
								usuarioRegistrado.getVendas().remove(vendaUsuario);
							}
						}
					}
				}
			}
			for (Veiculo veiculo : repositorioVeiculo.findAll()) {
				if (!veiculo.getVendas().isEmpty()) {
					for (Venda vendaVeiculo : veiculo.getVendas()) {
						if (vendaVeiculo.getId() == idVenda) {
							for (Veiculo veiculoRegistrado : veiculos) {
								veiculoRegistrado.getVendas().remove(vendaVeiculo);
							}
						}
					}
				}
			}
			empresas = repositorioEmpresa.findAll();
			usuarios = repositorioUsuario.findAll();
			veiculos = repositorioVeiculo.findAll();
			repositorio.deleteById(idVenda);
			return new ResponseEntity<>("Venda excluida :D", HttpStatus.ACCEPTED);
		}
	}
	
	@PutMapping("/atualizar/{idVenda}")
	public ResponseEntity<?> atualizarVendaID(@PathVariable Long idVenda, @RequestBody Venda dados) {
		Venda venda = repositorio.findById(idVenda).orElse(null);
		if (venda == null) {
			return new ResponseEntity<>("Venda não encontrada :/", HttpStatus.NOT_FOUND);
		} 
		else {
			if (dados != null) {
				if (dados.getIdentificacao() != null) {
					venda.setIdentificacao(dados.getIdentificacao());
				}
				repositorio.save(venda);
			}
			return new ResponseEntity<>(venda, HttpStatus.ACCEPTED);
		}
	}
}
