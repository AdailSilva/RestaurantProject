package com.projeto.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Pedido;
import com.projeto.repository.PedidoRepository;

@FacesConverter(forClass = Pedido.class)
public class PedidoConverter implements Converter {

	private PedidoRepository PedidoRepo;

	public PedidoConverter() {
		System.out.println("Pedido converter chamado");
		PedidoRepo = (PedidoRepository) CDI.current().select(PedidoRepository.class).get();

	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pedido retorno = null;

		if (value != null) {

			Integer id = new Integer(value);
			retorno = PedidoRepo.findBy(id);
			System.out.println("CONVERTER");
			System.out.println(retorno.getId());
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Pedido Pedido = (Pedido) value;
			return Pedido.getId() == null ? null : Pedido.getId().toString();
		}

		return "";
	}

}
