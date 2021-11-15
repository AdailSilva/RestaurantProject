package com.projeto.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.projeto.model.Pessoa;

public class UsuarioSistema extends User {
	private static final long serialVersionUID = 1L;

	private Pessoa pessoa;

	public UsuarioSistema(Pessoa pessoa, Collection<? extends GrantedAuthority> authorities) {
		super(pessoa.getUsuario(), pessoa.getSenha(), authorities);

		this.pessoa = pessoa;
	}

	public Pessoa getUsuario() {
		return pessoa;
	}

}
