package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.filter.FuncionarioFilter;
import com.adailsilva.model.Endereco;
import com.adailsilva.model.Funcionario;
import com.adailsilva.repository.EnderecoRepository;
import com.adailsilva.repository.FuncionarioRepository;
import com.adailsilva.service.FuncionarioService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class FuncionarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Funcionario funcionario = new Funcionario();

	@Getter
	@Setter
	private Funcionario funcionarioSelecionado = new Funcionario();

	@Getter
	@Setter
	private Endereco endereco = new Endereco();

	@Getter
	@Setter
	@Inject
	private FuncionarioRepository funcionarioRepo;

	@Inject
	private FuncionarioService funcionarioService;

	@Getter
	@Setter
	private List<Funcionario> lista = new ArrayList<>();

	@Getter
	@Setter
	private FuncionarioFilter filtro = new FuncionarioFilter();

	@Getter
	@Setter
	private List<Endereco> enderecos = new ArrayList<>();

	@Inject
	private EnderecoRepository enderecoRepo;

	@Getter
	@Setter
	private Integer funcionarioIDViewParam;

	public void inicializar() {

		if (funcionarioIDViewParam != null) {
			funcionario = funcionarioRepo.findBy(funcionarioIDViewParam);
			enderecos = funcionario.getEnderecos();
		}

	}

	/*
	 * 
	 * Tela novo funcionario
	 * 
	 */

	public void salvar() {

		if (funcionarioService.salvar(funcionario)) {
			enderecos.forEach(e -> {
				e.setPessoa(funcionario);
				enderecoRepo.save(e);
			});
			funcionario = new Funcionario();
			enderecos = new ArrayList<>();

		}

	}

	public void adicionarEndereco() {
		enderecos.add(endereco);
		endereco = new Endereco();
	}

	/*
	 * 
	 * Tela edição de funcionario
	 * 
	 */

	public void adicionarEnderecoEdicao() {
		endereco.setPessoa(funcionario);
		enderecoRepo.save(endereco);
		enderecos.add(endereco);
		endereco = new Endereco();
	}

	public void listados() {
		lista = funcionarioRepo.filtrados(filtro);
		System.out.println(lista.size());
	}

	public void remover() {
		try {
			funcionario = funcionarioRepo.findBy(funcionario.getId());
			funcionarioRepo.remove(funcionario);
			lista.remove(funcionario);
		} catch (javax.persistence.PersistenceException e) {

			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)

			{
				FacesUtil.addErrorMessage(
						"Este registro não pode ser deletado, pois está relacionado com outros registros .Consulte o ADM.");
			}

		}
	}

	public void editar() {
		if (funcionarioService.editar(funcionario)) {
			funcionario = new Funcionario();

		}
	}

	public void removerEndereco() {
		try {
			endereco = enderecoRepo.findBy(endereco.getId());
			enderecoRepo.remove(endereco);
			enderecos.remove(endereco);
			endereco = new Endereco();
		} catch (javax.persistence.PersistenceException e) {
			System.out.println("Lancou exception");
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)

			{
				FacesUtil.addErrorMessage(
						"Este registro não pode ser deletado, pois está relacionado com outros registros .Consulte o ADM.");
				System.out.println("entrei aqui");
			}

		}

	}

	public void editarEndereco() {
		endereco = enderecoRepo.save(endereco);
	}

	public void novoEndereco() {
		endereco = new Endereco();
	}

}
