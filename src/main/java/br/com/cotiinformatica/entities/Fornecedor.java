package br.com.cotiinformatica.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="fornecedor")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Fornecedor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idFornecedor")
	private Integer idFornecedor;
	
	@Column(name="nome", length= 150, nullable = false)
	private String nome;
	
	@Column(name="cnpj", length = 25, nullable = false, unique = true)
	private String cnpj;
	
	@OneToMany(mappedBy="fornecedor") //1 Fornecedor TEM muitos produtos
	private List<Produto> produtos;
}
