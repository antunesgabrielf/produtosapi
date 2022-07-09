package br.com.cotiinformatica.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FornecedorPutRequest {
	
	private Integer idFornecedor;
	private String nome;
	private String CNPJ;

}
