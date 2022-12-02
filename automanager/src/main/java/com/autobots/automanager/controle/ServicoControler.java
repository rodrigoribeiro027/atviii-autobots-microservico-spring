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
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/servico")
public class ServicoControler {
	@Autowired
	private RepositorioServico repositorio;
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	@Autowired
	private RepositorioVenda repositorioVenda;
	
	@GetMapping("/buscaServicos")
	public ResponseEntity<List<Servico>> buscarServicos(){
		List<Servico> servicos = repositorio.findAll();
		return new ResponseEntity<List<Servico>>(servicos, HttpStatus.FOUND);
	}
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Servico> buscarServicoID(@PathVariable Long id){
		Servico servico = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(servico == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Servico>(servico, status);
	}
	@DeleteMapping("/excluir/{idServico}")
	public ResponseEntity<?> excluirServicoID(@PathVariable Long idServico){
		List<Empresa> empresas = repositorioEmpresa.findAll();
		List<Venda> vendas = repositorioVenda.findAll();
		Servico verificador = repositorio.findById(idServico).orElse(null);
		if(verificador == null) {
			return new ResponseEntity<>("Servico não encontrado", HttpStatus.NOT_FOUND);
		}
		else {
			for(Empresa empresa: repositorioEmpresa.findAll()) {
				if(empresa.getServicos().size()>0) {
					for(Servico servicoEmpresa: empresa.getServicos()) {
						if(servicoEmpresa.getId() == idServico) {
							for(Empresa empresaRegistrado:empresas) {
								empresaRegistrado.getServicos().remove(servicoEmpresa);
							}
						}
					}
				}
			}
			for(Venda venda: repositorioVenda.findAll()) {
				if(venda.getServicos().size()>0) {
					for(Servico servicoVenda: venda.getServicos()) {
						if(servicoVenda.getId()== idServico) {
							for(Venda vendaRegistrada: vendas) {
								vendaRegistrada.getServicos().remove(servicoVenda);
							}
						}
					}
				}
			}
			repositorio.deleteById(idServico);
			return new ResponseEntity<>("Serviço excluido",HttpStatus.ACCEPTED);
		}
	}
	
	@PutMapping("/atualizar/{idServico}")
	public ResponseEntity<?> atualizarServico(@PathVariable Long idServico, @RequestBody Servico dados){
		Servico servico = repositorio.findById(idServico).orElse(null);
		if(servico == null) {
			return new ResponseEntity<>("Servico não encontrado", HttpStatus.NOT_FOUND);
		}
		else {
			if(dados != null) {
				if(dados.getNome() != null) {
					servico.setNome(dados.getNome());
				}
				if(dados.getDescricao() != null) {
					servico.setDescricao(dados.getDescricao());
				}
				servico.setValor(dados.getValor());
				repositorio.save(servico);
			}
			return new ResponseEntity<>(servico,HttpStatus.ACCEPTED);
		}
	}
	@PostMapping("/cadastro/{idEmpresa}")
	public ResponseEntity<Empresa> cadastrarServEmpresa(@RequestBody Servico dados, @PathVariable Long idEmpresa){
		dados.setOriginal(true);
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).orElse(null);
		HttpStatus status = null;
		if(empresa == null) {
			status = HttpStatus.NOT_FOUND;
		}
		else {
			empresa.getServicos().add(dados);
			repositorioEmpresa.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<Empresa>(empresa, status);
	}
}
