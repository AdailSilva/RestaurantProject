package com.projeto.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.projeto.model.Categoria;
import com.projeto.repository.CategoriaRepository;
import com.projeto.util.jsf.FacesUtil;

public class CategoriaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaRepository categoriaRepo;

	public boolean salvar(Categoria c) {

		try {
			categoriaRepo.findByDescricao(c.getDescricao());
			FacesUtil.addErrorMessage("Já existe uma categoria com este nome");
			return false;
		} catch (Exception e) {
			categoriaRepo.save(c);
			FacesUtil.addInfoMessage("Categoria salva com sucesso");
			e.printStackTrace();
			return true;
		}

	}

}
