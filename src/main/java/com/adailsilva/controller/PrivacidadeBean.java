package com.adailsilva.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.service.PessoaService;
import com.adailsilva.util.jsf.FacesUtil;

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
