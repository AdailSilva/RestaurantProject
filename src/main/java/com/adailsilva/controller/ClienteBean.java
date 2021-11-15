package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.filter.ClienteFilter;
import com.adailsilva.model.Cliente;
import com.adailsilva.model.Endereco;
import com.adailsilva.repository.ClienteRepository;
import com.adailsilva.repository.EnderecoRepository;
import com.adailsilva.service.ClienteService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ClienteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Cliente cliente = new Cliente();

	@Getter
	@Setter
	private Cliente clienteSelecionado = new Cliente();

	@Getter
	@Setter
	private Endereco endereco = new Endereco();

	@Getter
	@Setter
	@Inject
	private ClienteRepository clienteRepo;

	@Inject
	private ClienteService clienteService;

	@Getter
	@Setter
	private List<Cliente> lista = new ArrayList<>();

	@Getter
	@Setter
	private ClienteFilter filtro = new ClienteFilter();

	@Getter
	@Setter
	private List<Endereco> enderecos = new ArrayList<>();

	@Inject
	private EnderecoRepository enderecoRepo;

	@Getter
	@Setter
	private Integer clienteIDViewParam;

	public void inicializar() {

		if (clienteIDViewParam != null) {
			cliente = clienteRepo.findBy(clienteIDViewParam);
			enderecos = cliente.getEnderecos();
		}

	}

	/*
	 * 
	 * Tela novo cliente
	 * 
	 */

	public void salvar() {

		if (clienteService.salvar(cliente)) {
			enderecos.forEach(e -> {
				e.setPessoa(cliente);
				enderecoRepo.save(e);
			});
			cliente = new Cliente();
			enderecos = new ArrayList<>();

		}

	}

	public void adicionarEndereco() {
		enderecos.add(endereco);
		endereco = new Endereco();
	}

	/*
	 * 
	 * Tela edição de cliente
	 * 
	 */

	public void adicionarEnderecoEdicao() {
		endereco.setPessoa(cliente);
		enderecoRepo.save(endereco);
		enderecos.add(endereco);
		endereco = new Endereco();
	}

	public void listados() {
		lista = clienteRepo.filtrados(filtro);
		System.out.println(lista.size());
	}

	public void remover() {
		try {
			cliente = clienteRepo.findBy(cliente.getId());
			clienteRepo.remove(cliente);
			lista.remove(cliente);
		} catch (javax.persistence.PersistenceException e) {

			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)

			{
				FacesUtil.addErrorMessage(
						"Este registro não pode ser deletado, pois está relacionado com outros registros .Consulte o ADM.");
			}

		}
	}

	public void editar() {
		if (clienteService.editar(cliente)) {
			cliente = new Cliente();

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
