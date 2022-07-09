package br.com.cotiinformatica.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CriarContaPostRequest {
	
	private String nome;
	private String email;
	private String senha;
	

}
