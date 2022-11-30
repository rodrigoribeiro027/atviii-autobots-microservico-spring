package com.autobots.automanager.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.repositorios.RepositorioDocumento;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	
	@Autowired
	private RepositorioDocumento repositorio;
	
	@GetMapping("/buscarDocumentos")
	public ResponseEntity<List<Documento>> ObterDocumentos(){
		List<Documento> documentos = repositorio.findAll();
		return new ResponseEntity<List<Documento>>(documentos,HttpStatus.FOUND);
	}
	
	@GetMapping("/busca/{id}")
	public ResponseEntity<Documento> ObterDocumentoID(@PathVariable Long id){
		Documento documento = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(documento == null) 
		{
			status = HttpStatus.NOT_FOUND;
		}
		else {
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Documento>(documento,status);
	}
}
