package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import com.adailsilva.model.Acesso;
import com.adailsilva.model.ItemPedido;
import com.adailsilva.repository.ItemPedidoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class PermissoesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	@Getter
	private UsuarioSistema usuarioSistema;

	@Getter
	@Setter
	private List<String> acessos = new ArrayList<>();

	@Getter
	@Setter
	private boolean menuPrincipal = true;

	@Getter
	@Setter
	private boolean menuVenda = false;

	@Getter
	@Setter
	private List<String> teste = new ArrayList<>();

	@Getter
	@Setter
	private MenuModel model = new DefaultMenuModel();

	@Getter
	@Setter
	private Long numberCozinha = new Long(0);

	@Getter
	@Setter
	private Long numberGarcon = new Long(0);

	@Inject
	private ItemPedidoRepository itemPedidoRepo;

	@PostConstruct
	public void preencherTabela() {
		acessos.clear();
		for (Acesso a : usuarioSistema.getUsuario().getAcesso()) {
			acessos.add(a.getDescricao());
		}
	}

	public void alterar() {
		if (menuPrincipal == true) {
			menuPrincipal = false;
			menuVenda = true;
		} else {
			menuPrincipal = true;
			menuVenda = false;
		}

	}

	public void topBarProcess() {
		numberGarcon = itemPedidoRepo.pendentesGarconCount();
		numberCozinha = itemPedidoRepo.pendentesCozinhaCount();
	}

}
