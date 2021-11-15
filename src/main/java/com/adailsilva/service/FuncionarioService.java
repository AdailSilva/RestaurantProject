package com.adailsilva.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.adailsilva.model.Acesso;
import com.adailsilva.model.Funcionario;
import com.adailsilva.repository.AcessoRepository;
import com.adailsilva.repository.EnderecoRepository;
import com.adailsilva.repository.FuncionarioRepository;
import com.adailsilva.repository.RestauranteRepository;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

public class FuncionarioService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Inject
	private FuncionarioRepository funcionarioRepo;

	@Inject
	private RestauranteRepository restauranteRepo;

	@Inject
	private EnderecoRepository enderecoRepo;

	@Inject
	private AcessoRepository acessoRepo;

	@Inject
	private PessoaService pessoaService;

	public boolean salvar(Funcionario funcionario) {
		boolean condicao = pessoaService.salvar(funcionario);

		if (condicao) {

			Acesso a = acessoRepo.findByDescricao("VENDEDOR");
			funcionario.getAcesso().add(a);
			funcionario.setRestaurante(restauranteRepo.findBy(new Integer(1)));
			funcionario = funcionarioRepo.save(funcionario);
			funcionario.getEnderecos().forEach(e -> {
				enderecoRepo.save(e);
			});
			FacesUtil.addInfoMessage("Funcionario salvo com sucesso");
		}
		return condicao;
	}

	public boolean editar(Funcionario funcionario) {
		boolean condicao = pessoaService.editar(funcionario);
		if (condicao) {
			funcionario = funcionarioRepo.save(funcionario);
			FacesUtil.addInfoMessage("Funcionario editado com sucesso");
		}

		return condicao;
	}
}
