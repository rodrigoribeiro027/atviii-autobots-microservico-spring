package com.autobots.automanager.modeleos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controle.UsuarioControle;
import com.autobots.automanager.entitades.Usuario;



@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario>{

	@Override
	public void adicionarLink(List<Usuario> lista) {
		for(Usuario usuario:lista) {
			long id = usuario.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControle.class)
							.buscarUsuarioID(id))
					.withRel("Visualizar usuario de id " + id);
			usuario.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Usuario objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.buscarUsuarios())
				.withRel("Lista de usuarios");
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkUpdate(Usuario objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.atualizarUsuarioID(objeto.getId(), objeto))
				.withRel("Atualizar usuario de id " + objeto.getId());
		objeto.add(linkProprio);
	}

	@Override
	public void adicionarLinkDelete(Usuario objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.excluirUsuarioID(objeto.getId()))
				.withRel("Excluir usuario de id " + objeto.getId());
		objeto.add(linkProprio);
	}

}
