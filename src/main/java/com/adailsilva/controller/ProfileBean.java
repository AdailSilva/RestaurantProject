package com.adailsilva.controller;

import java.io.Serializable;

import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.exception.ConstraintViolationException;

import com.adailsilva.model.Cliente;
import com.adailsilva.model.Endereco;
import com.adailsilva.model.Funcionario;
import com.adailsilva.repository.ClienteRepository;
import com.adailsilva.repository.EnderecoRepository;
import com.adailsilva.repository.FuncionarioRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.service.ClienteService;
import com.adailsilva.service.FuncionarioService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@javax.faces.view.ViewScoped
public class ProfileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Getter
	@UsuarioLogado
	private UsuarioSistema usuario;

	@Getter
	@Setter
	private Cliente cliente;

	@Inject
	private ClienteService clienteServ;

	@Getter
	@Setter
	private Funcionario funcionario;

	@Inject
	private ClienteRepository clienteRepo;

	@Inject
	private FuncionarioRepository funcionarioRepo;

	@Getter
	@Setter
	private Endereco endereco = new Endereco();

	@Getter
	@Setter
	private Endereco enderecoEdicao = new Endereco();

	@Inject
	private EnderecoRepository enderecoRepo;

	@Inject
	private FuncionarioService funcionarioServ;

	public void preRender(ComponentSystemEvent event) {
		if (FacesUtil.isNotPostback()) {
			if (usuario.getUsuario() instanceof Cliente) {
				cliente = clienteRepo.findBy(usuario.getUsuario().getId());
			}
			if (usuario.getUsuario() instanceof Funcionario) {
				funcionario = funcionarioRepo.findBy(usuario.getUsuario().getId());
			}

		}

	}

	public void editarFuncionario() {
		try {
			funcionarioServ.editar(funcionario);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro");
		}

	}

	public void cadastrarEnderecoFuncionario() {
		try {
			endereco.setPessoa(funcionario);
			funcionario.getEnderecos().add(endereco);
			enderecoRepo.save(endereco);
			endereco = new Endereco();
			FacesUtil.addInfoMessage("Endereço cadastrado com sucesso");
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Ocorreu um erro ao cadastrar endereço");
		}

	}

	public void editarEnderecoFuncionario() {
		try {
			enderecoRepo.save(enderecoEdicao);
			FacesUtil.addInfoMessage("Endereço editado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro");
		}
	}

	public void removerFuncionario() {
		try {

			enderecoEdicao = enderecoRepo.findBy(enderecoEdicao.getId());
			enderecoRepo.remove(enderecoEdicao);
			funcionario.getEnderecos().remove(enderecoEdicao);
			FacesUtil.addInfoMessage("Endereço removido com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getCause() instanceof ConstraintViolationException) {
				FacesUtil
						.addErrorMessage("Este endereço não pode ser removido pois está relacionado com algum pedido.");
			} else {
				FacesUtil.addErrorMessage("Ocorreu um erro");
			}
		}

	}

	public void editarCliente() {
		try {
			clienteServ.editar(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro");
		}
	}

	public void cadastrarEnderecoCliente() {
		try {
			endereco.setPessoa(cliente);
			cliente.getEnderecos().add(endereco);
			enderecoRepo.save(endereco);
			endereco = new Endereco();
			FacesUtil.addInfoMessage("Endereço cadastrado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro ao cadastrar endereço");
		}

	}

	public void editarEnderecoCliente() {
		try {
			enderecoRepo.save(enderecoEdicao);
			FacesUtil.addInfoMessage("Endereço editado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro ao editar endereço");
		}
	}

	public void removerCliente() {
		try {

			enderecoEdicao = enderecoRepo.findBy(enderecoEdicao.getId());
			enderecoRepo.remove(enderecoEdicao);
			cliente.getEnderecos().remove(enderecoEdicao);
			enderecoEdicao = null;
			FacesUtil.addInfoMessage("Endereço removido com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getCause() instanceof ConstraintViolationException) {
				FacesUtil
						.addErrorMessage("Este endereço não pode ser removido pois está relacionado com algum pedido.");
			} else {
				FacesUtil.addErrorMessage("Ocorreu um erro");
			}
		}

	}

	public String getProfileURL() {
		if (usuario.getUsuario() instanceof Cliente) {
			return "profileCliente.xhtml";
		}
		return "profileFuncionario.xhtml";
	}

}
