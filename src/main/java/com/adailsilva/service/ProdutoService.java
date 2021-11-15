package com.adailsilva.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.adailsilva.model.Produto;
import com.adailsilva.repository.ProdutoRepository;
import com.adailsilva.util.jsf.FacesUtil;

public class ProdutoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoRepository produtoRepo;

	public boolean salvar(Produto p) {
		boolean condicao = false;
		try {
			produtoRepo.findByDescricao(p.getDescricao());
			FacesUtil.addErrorMessage("Já existe um produto cadastrado com esse nome");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			condicao = true;

		}

		if (condicao) {
			if (p.getCusto().doubleValue() > p.getPreco().doubleValue()) {
				FacesUtil.addErrorMessage("O custo não pode ser maior que o preço do produto");
				return false;
			} else {
				p.setUrlImagem("teste");
				produtoRepo.save(p);
				FacesUtil.addInfoMessage("Produto cadastrado com sucesso");
				return true;
			}
		}

		return false;

	}

}
