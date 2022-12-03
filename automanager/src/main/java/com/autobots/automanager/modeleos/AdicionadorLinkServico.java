package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.ServicoControler;
import com.autobots.automanager.entitades.Servico;



@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico>{

	@Override
	public void adicionarLink(List<Servico> lista) {
		for(Servico servico:lista) {
			long id = servico.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ServicoControler.class)
							.buscarServicoID(id))
					.withRel("Visualizar servico de id " + id);
			servico.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Servico objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControler.class)
						.buscarServicos())
				.withRel("Lista de servicos");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Servico objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControler.class)
						.atualizarServico(objeto.getId(), objeto))
				.withRel("Atualizar servico de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Servico objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControler.class)
						.excluirServicoID(objeto.getId()))
				.withRel("Excluir servico de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
