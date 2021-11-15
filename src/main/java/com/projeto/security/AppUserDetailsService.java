package com.projeto.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.spi.CDI;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.projeto.model.Acesso;
import com.projeto.model.Pessoa;
import com.projeto.repository.PessoaRepository;

public class AppUserDetailsService implements UserDetailsService {

	public AppUserDetailsService() {
		System.out.println("Construtor chamado");
	}

	@Override
	public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
		PessoaRepository pessoaRepo = (PessoaRepository) CDI.current().select(PessoaRepository.class).get();
		Pessoa pessoa = pessoaRepo.findByUsuario(usuario);
		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(pessoa, getGrupos(pessoa));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Pessoa pessoa) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Acesso grupo : pessoa.getAcesso()) {
			authorities.add(new SimpleGrantedAuthority(grupo.getDescricao().toUpperCase()));
		}

		return authorities;
	}

}
