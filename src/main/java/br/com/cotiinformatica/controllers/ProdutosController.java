package br.com.cotiinformatica.controllers;

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

import br.com.cotiinformatica.entities.Produto;
import br.com.cotiinformatica.repositories.IProdutoRepository;
import br.com.cotiinformatica.requests.ProdutoPostRequest;
import br.com.cotiinformatica.requests.ProdutoPutRequest;

@Transactional
@Controller
public class ProdutosController {
		
		@Autowired
		private IProdutoRepository produtoRepository;
	
		@RequestMapping(value="/api/produtos", method = RequestMethod.POST)
		public ResponseEntity<String> post(@RequestBody ProdutoPostRequest request){
				
			try {
				
				Produto prod = new Produto();
				
				prod.setNome(request.getNome());
				prod.setDescricao(request.getDescricao());
				prod.setPreco(request.getPreco());
				prod.setQuantidade(request.getQuantidade());
			
				produtoRepository.save(prod);
				
				return ResponseEntity
						.status(HttpStatus.CREATED)
						.body("Produto cadastrado com sucesso.");
			}
			
			catch(Exception e) {
			
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			
			}			
		}
		
		@RequestMapping(value="/api/produtos", method = RequestMethod.PUT)
		public ResponseEntity<String> put(@RequestBody ProdutoPutRequest request){
			
			try {
			
			Produto prod = new Produto();
			
			prod.setIdProduto(request.getIdProduto());
			prod.setNome(request.getNome());
			prod.setDescricao(request.getDescricao());
			prod.setPreco(request.getPreco());
			prod.setQuantidade(request.getQuantidade());
			
			produtoRepository.save(prod);
				
			return ResponseEntity.status(HttpStatus.OK).body("produto atualizado com sucesso.");
			
			}
			
			catch(Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			}
		}

		@RequestMapping(value="/api/produtos/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<String> delete(@PathVariable("id") Integer id){
			
			try {
			
				//consultando o produto no banco de dados através do ID
				Optional<Produto> consulta = produtoRepository.findById(id);
				
				if(consulta.isPresent()) {
					
					Produto prod = consulta.get();
					produtoRepository.delete(prod);
					
					return ResponseEntity.status(HttpStatus.OK).body("Produto excluido com sucesso.");
				}
				else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produto nao encontrado. Verifique o ID informado.");
				}
				
			}
			catch(Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			}
		}
		
		@RequestMapping(value="/api/produtos", method = RequestMethod.GET)
		public ResponseEntity<List<Produto>> getAll(){
			try {
				
				//obtendo uma lista de produtos do banco de dados
				List<Produto> prod = (List<Produto>) produtoRepository.findAll();
				
				return ResponseEntity.status(HttpStatus.OK).body(prod);
		
			}
			catch(Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
			
		}

		@RequestMapping(value="/api/produtos/{id}", method = RequestMethod.GET)
		public ResponseEntity<Produto> getById(@PathVariable("id") Integer id){
			
			try {
				
				Optional<Produto> consulta = produtoRepository.findById(id);
				
				if(consulta.isPresent()) {
					
					Produto prod = consulta.get();
					return ResponseEntity.status(HttpStatus.OK).body(prod);
					
				}
				else {
					
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
					
				}
			
			}
			catch(Exception e) {
				
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			
			}
		}

}