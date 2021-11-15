package com.projeto.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import com.projeto.model.Acesso;
import com.projeto.model.Cliente;
import com.projeto.model.Funcionario;
import com.projeto.repository.AcessoRepository;
import com.projeto.repository.ClienteRepository;
import com.projeto.repository.EnderecoRepository;
import com.projeto.repository.RestauranteRepository;
import com.projeto.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

public class ClienteService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Inject
	private ClienteRepository clienteRepo;

	@Inject
	private RestauranteRepository restauranteRepo;

	@Inject
	private AcessoRepository acessoRepo;

	@Inject
	private EnderecoRepository enderecoRepo;

	@Inject
	private PessoaService pessoaService;

	@Transactional
	public boolean salvar(Cliente cliente) {
		boolean condicao = pessoaService.salvar(cliente);

		if (condicao) {
			Acesso a = acessoRepo.findByDescricao("CLIENTE");
			cliente.getAcesso().add(a);
			cliente.setRestaurante(restauranteRepo.findBy(new Integer(1)));
			cliente.setDataCriacao(new Date());
			cliente = clienteRepo.save(cliente);
			cliente.getEnderecos().forEach(e -> {
				enderecoRepo.save(e);
			});
			FacesUtil.addInfoMessage("Cliente salvo com sucesso");
			
		}

		return condicao;

	}

	public boolean editar(Cliente cliente) {
		boolean condicao = pessoaService.editar(cliente);
		if (condicao) {
			cliente = clienteRepo.save(cliente);
			FacesUtil.addInfoMessage("Cliente editado com sucesso");
		}

		return condicao;
	}
}
