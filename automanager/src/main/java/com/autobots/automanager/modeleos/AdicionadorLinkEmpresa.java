package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.EmpresaControle;
import com.autobots.automanager.entitades.Empresa;



@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa>{

	@Override
	public void adicionarLink(List<Empresa> lista) {
		for(Empresa empresa:lista) {
			long id = empresa.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(EmpresaControle.class)
							.buscarEmpresaID(id))
					.withRel("Visualizar empresa de id " + id);
			empresa.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Empresa objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.buscarEmpresas())
				.withRel("Lista de empresas");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Empresa objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.atualizarEmpresa(objeto.getId(), objeto))
				.withRel("Atualizar empresa de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Empresa objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.excluirEmpresaID(objeto.getId()))
				.withRel("Excluir empresa de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
