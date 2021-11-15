package com.projeto.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Cliente;
import com.projeto.repository.ClienteRepository;

@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter {

	private ClienteRepository categoriaRepo;

	public ClienteConverter() {
		System.out.println("Cliente converter chamado");
		categoriaRepo = (ClienteRepository) CDI.current().select(ClienteRepository.class).get();
		System.out.println(categoriaRepo == null ? "sim" : "nao");

	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Cliente retorno = null;

		if (value != null) {

			Integer id = new Integer(value);
			retorno = categoriaRepo.findBy(id);

		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Cliente categoria = (Cliente) value;
			return categoria.getId() == null ? null : categoria.getId().toString();
		}

		return "";
	}

}
