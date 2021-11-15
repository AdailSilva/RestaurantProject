package com.adailsilva.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.adailsilva.model.Pessoa;
import com.adailsilva.repository.PessoaRepository;
import com.adailsilva.util.jsf.FacesUtil;

public class PessoaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository pessoaRepo;

	public boolean salvar(Pessoa p) {
		boolean salvar = false;

		try {
			System.out.println(p.getCpf() + "    CPF ");
			pessoaRepo.findByCpf(p.getCpf());
			FacesUtil.addErrorMessage("Este cliente já está em nossa base de dados, cadastrado com este cpf.");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			salvar = true;
		}
		try {
			pessoaRepo.findByUsuario(p.getUsuario());
			FacesUtil.addErrorMessage("Este nome de usuário já existe .");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			salvar = true;
		}

		return salvar;
	}

	public boolean editar(Pessoa p) {
		boolean editar = true;
		Pessoa pessoa = pessoaRepo.findBy(p.getId());
		if (!pessoa.getUsuario().equals(p.getUsuario())) {
			try {
				pessoaRepo.findByUsuario(p.getUsuario());
				FacesUtil.addErrorMessage("Este nome de usuário já existe .");
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				editar = true;
			}
		}else{
			p.setUsuario(pessoa.getUsuario());
		}

		if (!pessoa.getCpf().equals(p.getCpf())) {
			try {

				System.out.println(p.getCpf() + "    CPF ");
				pessoaRepo.findByCpf(p.getCpf());
				FacesUtil.addErrorMessage("Este cliente já está em nossa base de dados, cadastrado com este cpf.");
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				editar = true;
			}
		}
		return editar;
	}

	public void editarSenha(Pessoa p, String senhaAntiga, String senhaNova) {

		if (p.getSenha().equals(senhaAntiga)) {
			p.setSenha(senhaNova);
			pessoaRepo.save(p);
			FacesUtil.addInfoMessage("Senha alterada com sucesso !  ");
		} else {
			FacesUtil.addErrorMessage("A senha digitada é diferente da atual.");
		}

	}

}
