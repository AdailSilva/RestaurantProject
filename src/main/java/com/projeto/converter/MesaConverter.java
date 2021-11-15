package com.projeto.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Mesa;
import com.projeto.repository.MesaRepository;

@FacesConverter(forClass = Mesa.class)
public class MesaConverter implements Converter {

	private MesaRepository MesaRepo;

	public MesaConverter() {
		System.out.println("Mesa converter chamado");
		MesaRepo = (MesaRepository) CDI.current().select(MesaRepository.class).get();
		System.out.println(MesaRepo == null ? "sim" : "nao");

	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Mesa retorno = null;

		if (value != null) {

			Integer id = new Integer(value);
			retorno = MesaRepo.findBy(id);

		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Mesa Mesa = (Mesa) value;
			return Mesa.getId() == null ? null : Mesa.getId().toString();
		}

		return "";
	}

}
