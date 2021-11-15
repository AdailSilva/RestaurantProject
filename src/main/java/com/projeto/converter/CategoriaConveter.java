package com.projeto.converter;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.projeto.model.Categoria;
import com.projeto.repository.CategoriaRepository;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConveter implements Converter {

	private CategoriaRepository categoriaRepo;

	public CategoriaConveter() {
		System.out.println("Categoria converter chamado");
		categoriaRepo = (CategoriaRepository) CDI.current().select(CategoriaRepository.class).get();
		System.out.println(categoriaRepo == null ? "sim" : "nao");

	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Categoria retorno = null;

		if (value != null) {

			Integer id = new Integer(value);
			retorno = categoriaRepo.findBy(id);

		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Categoria categoria = (Categoria) value;
			return categoria.getId() == null ? null : categoria.getId().toString();
		}

		return "";
	}

}
