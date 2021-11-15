package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.model.Funcionario;
import com.adailsilva.model.Meta;
import com.adailsilva.repository.FuncionarioRepository;
import com.adailsilva.repository.MetaRepository;
import com.adailsilva.service.MetaService;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class MetasBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Meta meta = new Meta();

	@Getter
	@Setter
	private List<Funcionario> funcionarios = new ArrayList<>();

	@Inject
	private FuncionarioRepository funcionarioRepo;

	@Getter
	@Setter
	private Funcionario funcionarioSelecionado;

	@Inject
	private MetaRepository metaRepo;

	@Getter
	@Setter
	private List<Meta> metas = new ArrayList<>();

	@Getter
	private boolean chooseOption = true;

	@PostConstruct
	public void inicializar() {
		try {
			funcionarios = funcionarioRepo.findAll();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Inject
	private MetaService service;

	public void salvar() {
		if (service.salvar(funcionarioSelecionado, meta)) {
			meta = new Meta();
			funcionarioSelecionado = null;
		}

	}

	public void nova() {
		chooseOption = true;
	}

	public void listar() {
		try {
			metas = metaRepo.findAll();
			service.atualizarValoresParciais();
			metas.forEach(e -> {
				System.out.println(e.getId() + " " + e.getPorcentagem());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		chooseOption = false;
		System.out.println(chooseOption);

	}

}
