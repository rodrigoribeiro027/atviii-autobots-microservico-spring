package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.VendaControle;
import com.autobots.automanager.entitades.Venda;



@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda>{

	@Override
	public void adicionarLink(List<Venda> lista) {
		for(Venda venda:lista) {
			long id = venda.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(VendaControle.class)
							.buscarVendaID(id))
					.withRel("Visualizar Venda de id " + id);
			venda.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Venda objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.buscarVendas())
				.withRel("Lista de Vendas");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Venda objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.atualizarVendaID(objeto.getId(), objeto))
				.withRel("Atualizar Venda de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Venda objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.excluirVendaID(objeto.getId()))
				.withRel("Excluir venda de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
