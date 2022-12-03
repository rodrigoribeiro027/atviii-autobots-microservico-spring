package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.VeiculoControle;
import com.autobots.automanager.entitades.Veiculo;



@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo>{

	@Override
	public void adicionarLink(List<Veiculo> lista) {
		for(Veiculo veiculo:lista) {
			long id = veiculo.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(VeiculoControle.class)
							.buscarVeiculo(id))
					.withRel("Visualizar veiculo de id " + id);
			veiculo.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Veiculo objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VeiculoControle.class)
						.obterVeiculos())
				.withRel("Listagem de veiculos");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Veiculo objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VeiculoControle.class)
						.atualizarVeiculoID(objeto.getId(), objeto))
				.withRel("Atualizar Veiculo de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Veiculo objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VeiculoControle.class)
						.excluirVeiculoID(objeto.getId()))
				.withRel("Excluir Veiculo de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
