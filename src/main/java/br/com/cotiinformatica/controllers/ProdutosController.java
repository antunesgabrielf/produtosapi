package br.com.cotiinformatica.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.cotiinformatica.entities.Fornecedor;
import br.com.cotiinformatica.entities.Produto;
import br.com.cotiinformatica.repositories.IFornecedorRepository;
import br.com.cotiinformatica.repositories.IProdutoRepository;
import br.com.cotiinformatica.requests.ProdutoPostRequest;
import br.com.cotiinformatica.requests.ProdutoPutRequest;
import br.com.cotiinformatica.responses.ProdutoGetResponse;

@Transactional
@Controller
public class ProdutosController {

	@Autowired // autoinicialização
	private IProdutoRepository produtoRepository;

	@Autowired // autoinicialização
	private IFornecedorRepository fornecedorRepository;

	@RequestMapping(value = "/api/produtos", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody ProdutoPostRequest request) {

		try {

			// Obter o fornecedor através do ID no Banco de dados
			Optional<Fornecedor> consulta = fornecedorRepository.findById(request.getIdFornecedor());
			// verificar se o fornecedor não foi encontrado
			if (consulta.isEmpty()) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Fornecedor não encontrado, verifique o ID informado");

			}

			Produto prod = new Produto();

			prod.setNome(request.getNome());
			prod.setDescricao(request.getDescricao());
			prod.setPreco(request.getPreco());
			prod.setQuantidade(request.getQuantidade());
			prod.setFornecedor(consulta.get());

			produtoRepository.save(prod);

			return ResponseEntity.status(HttpStatus.CREATED).body("Produto cadastrado com sucesso.");
		}

		catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	@RequestMapping(value = "/api/produtos", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody ProdutoPutRequest request) {

		try {

			// consulta o fornecedor no banco de dados através do ID
			Optional<Fornecedor> consulta = fornecedorRepository.findById(request.getIdFornecedor());

			// verificar se fornecedor não foi encontrado
			if (consulta.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Fornecedor não foi encontrado, verifica o ID informado");
			}

			Produto prod = new Produto();

			prod.setIdProduto(request.getIdProduto());
			prod.setNome(request.getNome());
			prod.setDescricao(request.getDescricao());
			prod.setPreco(request.getPreco());
			prod.setQuantidade(request.getQuantidade());
			prod.setFornecedor(consulta.get()); // associando o produto ao fornecedor

			produtoRepository.save(prod);

			return ResponseEntity.status(HttpStatus.OK).body("produto atualizado com sucesso.");

		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/api/produtos/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable("id") Integer id) {

		try {

			// consultando o produto no banco de dados através do ID
			Optional<Produto> consulta = produtoRepository.findById(id);

			if (consulta.isPresent()) {

				Produto prod = consulta.get();
				produtoRepository.delete(prod);

				return ResponseEntity.status(HttpStatus.OK).body("Produto excluido com sucesso.");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Produto nao encontrado. Verifique o ID informado.");
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/api/produtos", method = RequestMethod.GET)
	public ResponseEntity<List<ProdutoGetResponse>> getAll() {
		try {

			// obtendo uma lista de produtos do banco de dados
			List<Produto> prod = (List<Produto>) produtoRepository.findAll();

			List<ProdutoGetResponse> list = new ArrayList<ProdutoGetResponse>();

			// Percorre os produtos obtidos no banco de dados
			for (Produto produto : prod) {

				ProdutoGetResponse response = new ProdutoGetResponse();

				response.setIdProduto(produto.getIdProduto());
				response.setNome(produto.getNome());
				response.setPreco(produto.getPreco());
				response.setQuantidade(produto.getQuantidade());
				response.setTotal(produto.getPreco() * produto.getQuantidade());
				response.setDescricao(produto.getDescricao());
				response.setIdFornecedor(produto.getFornecedor().getIdFornecedor());
				response.setNomeFornecedor(produto.getFornecedor().getNome());
				response.setCnpjFornecedor(produto.getFornecedor().getCnpj());

				list.add(response);

			}

			return ResponseEntity.status(HttpStatus.OK).body(list);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@RequestMapping(value = "/api/produtos/{id}", method = RequestMethod.GET)
	public ResponseEntity<ProdutoGetResponse> getById(@PathVariable("id") Integer id) {

		try {

			Optional<Produto> consulta = produtoRepository.findById(id);

			if (consulta.isPresent()) {

				Produto prod = consulta.get();

				ProdutoGetResponse response = new ProdutoGetResponse();

				response.setIdProduto(prod.getIdProduto());
				response.setNome(prod.getNome());
				response.setPreco(prod.getPreco());
				response.setQuantidade(prod.getQuantidade());
				response.setTotal(prod.getPreco() * prod.getQuantidade());
				response.setDescricao(prod.getDescricao());
				response.setIdFornecedor(prod.getFornecedor().getIdFornecedor());
				response.setNomeFornecedor(prod.getFornecedor().getNome());
				response.setCnpjFornecedor(prod.getFornecedor().getCnpj());

				return ResponseEntity.status(HttpStatus.OK).body(response);

			} else {

				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

			}

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}

	@RequestMapping(value = "/api/produtos/obter-por-nome/{nome}", method = RequestMethod.GET)
	public ResponseEntity<List<ProdutoGetResponse>> findByNome(@PathVariable("nome") String nome) {
		
		try {
			
			List<Produto> produtos = (List<Produto>) produtoRepository.findByNome(nome);			
			List<ProdutoGetResponse> lista = new ArrayList<ProdutoGetResponse>();
			
			for(Produto produto : produtos) {
				
				ProdutoGetResponse response = new ProdutoGetResponse();
				
				response.setIdProduto(produto.getIdProduto());
				response.setNome(produto.getNome());
				response.setPreco(produto.getPreco());
				response.setQuantidade(produto.getQuantidade());
				response.setTotal(produto.getPreco() * produto.getQuantidade());
				response.setDescricao(produto.getDescricao());
				response.setIdFornecedor(produto.getFornecedor().getIdFornecedor());
				response.setNomeFornecedor(produto.getFornecedor().getNome());
				response.setCnpjFornecedor(produto.getFornecedor().getCnpj());
				
				lista.add(response);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(lista);			
		}
		catch(Exception e) {
			
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}