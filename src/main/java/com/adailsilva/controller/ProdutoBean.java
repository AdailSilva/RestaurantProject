package com.adailsilva.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.filter.ProdutoFilter;
import com.adailsilva.model.Categoria;
import com.adailsilva.model.Produto;
import com.adailsilva.repository.CategoriaRepository;
import com.adailsilva.repository.ProdutoRepository;
import com.adailsilva.service.ProdutoService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class ProdutoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Produto produto = new Produto();

	@Inject
	private ProdutoRepository produtoRepo;

	@Inject
	private CategoriaRepository categoriaRepo;

	@Getter
	@Setter
	private List<Categoria> categorias;

	@Getter
	private List<Produto> produtosFiltrados;

	@Getter
	@Setter
	@Inject
	private ProdutoFilter filter;

	@Getter
	@Setter
	private Produto produtoSelecionado = new Produto();

	@Inject
	private ProdutoService produtoService;

	public ProdutoBean() {
		produtoSelecionado.setCategoria(new Categoria());
	}

	/*
	 * Tela novo produto
	 */

	public void carregarCategorias(ComponentSystemEvent event) {
		if (FacesUtil.isNotPostback()) {
			System.out.println("PRODUTO CHAMADO");
			categorias = categoriaRepo.findAll();
		}

	}

	public void guardar() {
		try {
			if (produtoService.salvar(produto)) {
				produto = new Produto();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Ocorreu um erro.");
		}

	}

	/*
	 * Tela edição de cliente
	 */

	public void pesquisar() {
		produtosFiltrados = produtoRepo.filtrados(filter);
	}

	public void remover() {
		try {
			produtoSelecionado = produtoRepo.findBy(produtoSelecionado.getId());
			produtoRepo.remove(produtoSelecionado);
			produtosFiltrados.remove(produtoSelecionado);
		} catch (javax.persistence.PersistenceException e) {

			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)

			{
				FacesUtil.addErrorMessage(
						"Este registro não pode ser deletado, pois está relacionado com outros registros .Consulte o ADM.");
			}

		}
	}

	public void editar() {
		produtoSelecionado = produtoRepo.save(produtoSelecionado);
	}
}
