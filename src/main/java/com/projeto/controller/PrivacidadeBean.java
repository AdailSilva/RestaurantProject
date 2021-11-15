package com.projeto.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.security.UsuarioLogado;
import com.projeto.security.UsuarioSistema;
import com.projeto.service.PessoaService;
import com.projeto.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class PrivacidadeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaService pessoaService;

	@Getter
	@Setter
	private String novaSenha;

	@Getter
	@Setter
	private String senhaAntiga;

	@Inject
	@Getter
	@UsuarioLogado
	private UsuarioSistema user;

	public void editarSenha() {
		try {
			pessoaService.editarSenha(user.getUsuario(), senhaAntiga, novaSenha);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro");
		}
	}
}
