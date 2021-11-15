package com.projeto.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Endereco;
import com.projeto.repository.EnderecoRepository;

@FacesConverter(forClass = Endereco.class)
public class EnderecoConverter implements Converter {

	private EnderecoRepository enderecoRepo;

	public EnderecoConverter() {
		enderecoRepo = (EnderecoRepository) CDI.current().select(EnderecoRepository.class).get();

	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Endereco endereco = null;

		if (value != null) {

			Integer id = new Integer(value);
			endereco = enderecoRepo.findBy(id);

		}

		return endereco;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Endereco endereco = (Endereco) value;
			return endereco.getId() == null ? null : endereco.getId().toString();
		}

		return "";
	}

}
