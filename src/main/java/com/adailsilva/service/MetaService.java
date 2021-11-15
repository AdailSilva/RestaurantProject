package com.adailsilva.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import com.adailsilva.model.Funcionario;
import com.adailsilva.model.Meta;
import com.adailsilva.model.Pessoa;
import com.adailsilva.model.Meta.StatusMeta;
import com.adailsilva.repository.MetaRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.util.jsf.FacesUtil;

public class MetaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@UsuarioLogado
	@Inject
	private UsuarioSistema user;
	@Inject
	private MetaRepository metaRepo;

	@Inject
	private PedidoRepository pedidoRepo;

	public boolean salvar(Pessoa funcionarioSelecionado, Meta meta) {
		try {

			if (meta.getDataInicial().after(meta.getDataFinal())) {
				FacesUtil.addErrorMessage("Data final precisa ser maior que data inicial");
				return false;
			} else {

				if (meta.getValorTotal().doubleValue() > 0) {
					if (funcionarioSelecionado != null) {
						meta.setFuncionario(funcionarioSelecionado);
					}
					meta.setRestaurante(user.getUsuario().getRestaurante());
					meta.setStatus(StatusMeta.ANDAMENTO);
					metaRepo.save(meta);
					FacesUtil.addInfoMessage("Meta salva com sucesso");
				} else {

					FacesUtil.addErrorMessage("Valor total precisa ser maior que zero");
					return false;
				}

			}

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void atualizarValoresParciais() {

		for (Meta m : metaRepo.findbyStatus(StatusMeta.ANDAMENTO)) {
			BigDecimal valorTotal = BigDecimal.ZERO;
			if (m.getFuncionario() == null) {
				valorTotal = pedidoRepo.findBetweenDates(m.getDataInicial(), m.getDataFinal());
			} else {
				valorTotal = pedidoRepo.findBetweenDatesFunc(m.getDataInicial(), m.getDataFinal(),
						(Funcionario) m.getFuncionario());
			}
			if (valorTotal != null) {
				m.setValorCorrente(valorTotal);
			}

			if (new Date().after(m.getDataFinal())||m.getValorCorrente().doubleValue()>m.getValorTotal().doubleValue()) {
				m.setStatus(StatusMeta.FINALIZADO);
			}
						metaRepo.save(m);

		}

	}

}
