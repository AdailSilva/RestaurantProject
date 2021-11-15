package com.projeto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.filter.CategoriaFilter;
import com.projeto.model.Categoria;
import com.projeto.repository.CategoriaRepository;
import com.projeto.service.CategoriaService;
import com.projeto.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class CategoriaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Categoria categoria = new Categoria();

	@Inject
	private CategoriaRepository categoriaRepo;

	@Getter
	@Setter
	private Categoria categoriaSelecionada;

	@Getter
	@Setter
	private List<Categoria> filtrados = new ArrayList<Categoria>();

	@Getter
	@Setter
	private CategoriaFilter filter = new CategoriaFilter();

	@Inject
	private CategoriaService categoriaService;

	public void guardar() {

		try {
			if (categoriaService.salvar(categoria)) {
				categoria = new Categoria();
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro ! ");
		}

	}

	public void pesquisar() {
		filtrados = categoriaRepo.filtrados(filter);
	}

	public void remover() {
		try {
			categoriaSelecionada = categoriaRepo.findBy(categoriaSelecionada.getId());
			categoriaRepo.remove(categoriaSelecionada);
			filtrados.remove(categoriaSelecionada);
		} catch (javax.persistence.PersistenceException e) {

			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)

			{
				FacesUtil.addErrorMessage(
						"Este registro não pode ser deletado, pois está relacionado com outros registros .Consulte o ADM.");
			}

		}
	}

	public void editar() {
		categoriaSelecionada = categoriaRepo.save(categoriaSelecionada);
	}

}
