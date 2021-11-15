package com.projeto.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Funcionario;
import com.projeto.repository.FuncionarioRepository;

@FacesConverter(forClass = Funcionario.class)
public class FuncionarioConverter implements Converter {

	private FuncionarioRepository categoriaRepo;

	public FuncionarioConverter() {
		System.out.println("Funcionario converter chamado");
		categoriaRepo = (FuncionarioRepository) CDI.current().select(FuncionarioRepository.class).get();
		System.out.println(categoriaRepo == null ? "sim" : "nao");

	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Funcionario retorno = null;

		if (value != null) {

			Integer id = new Integer(value);
			retorno = categoriaRepo.findBy(id);

		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Funcionario categoria = (Funcionario) value;
			return categoria.getId() == null ? null : categoria.getId().toString();
		}

		return "";
	}

}
