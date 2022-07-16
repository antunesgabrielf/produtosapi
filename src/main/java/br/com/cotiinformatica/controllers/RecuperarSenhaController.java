package br.com.cotiinformatica.controllers;

import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.helpers.EmailHelper;
import br.com.cotiinformatica.helpers.MD5Helper;
import br.com.cotiinformatica.repositories.IUsuarioRepository;
import br.com.cotiinformatica.requests.RecuperarSenhaPostRequest;

@Controller
@Transactional
public class RecuperarSenhaController {
	
	@Autowired
	private IUsuarioRepository usuarioRepository;

	@RequestMapping(value = "/api/recuperar-senha", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody RecuperarSenhaPostRequest request) {
		
		try {
			
			//buscar o usuário no banco de dados atraves do email
			Usuario usuario = usuarioRepository.findByEmail(request.getEmail());
			
			//verificar se o usuário não foi encontrado
			if(usuario == null) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Email inválido. Usuário não encontrado.");
			}
			
			//gerando uma nova senha para o usuário:
			String novaSenha = String.valueOf(new Random().nextInt(999999999));
			
			EmailHelper.sendMessage(usuario.getEmail(), "Recuperação de Senha - Sistema de controle de produtos", 
					"Olá, " + usuario.getNome() + 
					"\n\nSua senha foi redefinida com sucesso para que você possa acessar o sistema de controle de produtos." + 
					"\nUtilize a senha: " + novaSenha + 
					"\n\nAtt\nEquipe COTI Informática.");
			
			usuario.setSenha(MD5Helper.encrypt(novaSenha));
			
			//salvar a nova senha do usuário no banco de dados
			usuarioRepository.save(usuario);
						
			return ResponseEntity
					.status(HttpStatus.OK)
					.body("Nova senha enviada para o email " + usuario.getEmail() + " com sucesso.");
		}
		catch (Exception e) {
		
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}

}