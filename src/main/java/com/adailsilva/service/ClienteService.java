package com.adailsilva.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import com.adailsilva.model.Acesso;
import com.adailsilva.model.Cliente;
import com.adailsilva.model.Funcionario;
import com.adailsilva.repository.AcessoRepository;
import com.adailsilva.repository.ClienteRepository;
import com.adailsilva.repository.EnderecoRepository;
import com.adailsilva.repository.RestauranteRepository;
import com.adailsilva.util.jsf.FacesUtil;

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
