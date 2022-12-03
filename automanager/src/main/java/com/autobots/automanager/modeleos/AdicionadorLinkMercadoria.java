package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.MercadoriaControle;
import com.autobots.automanager.entitades.Mercadoria;



@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria>{

	@Override
	public void adicionarLink(List<Mercadoria> lista) {
		for(Mercadoria mercadoria:lista) {
			long id = mercadoria.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(MercadoriaControle.class)
							.buscarMercadoriaID(id))
					.withRel("Visualizar mercadoria de id " + id);
			mercadoria.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Mercadoria objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(MercadoriaControle.class)
						.buscarMercadorias())
				.withRel("Lista de clientes");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Mercadoria objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
				.methodOn(MercadoriaControle.class)
				.atualizarMercadoriaID(objeto.getId(), objeto))
				.withRel("Atualizar Mercadoria de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Mercadoria objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(MercadoriaControle.class)
						.excluirMercadoriaEmpresa(objeto.getId()))
				.withRel("Excluir Mercadoria de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
