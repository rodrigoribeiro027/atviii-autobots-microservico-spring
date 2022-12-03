package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.DocumentoControle;
import com.autobots.automanager.entitades.Documento;


@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento>{

	@Override
	public void adicionarLink(List<Documento> lista) {
		for(Documento documento:lista) {
			long id = documento.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(DocumentoControle.class)
							.ObterDocumentoID(id))
					.withRel("Visualizar documento de id " + id);
			documento.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Documento objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.ObterDocumentos())
				.withRel("Lista de documentos");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Documento objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.atualizarDocumentoID(objeto.getId(), objeto))
				.withRel("Atualizar documento de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Documento objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.excluirDocumento(objeto.getId()))
				.withRel("Excluir documento de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
