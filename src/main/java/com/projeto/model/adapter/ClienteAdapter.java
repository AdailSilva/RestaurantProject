package com.projeto.model.adapter;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class ClienteAdapter {

	@Getter
	@Setter
	private Date data;
	@Getter
	@Setter
	private Long numeros;

	public ClienteAdapter(Date data, Long numeros) {
		this.data = data;
		this.numeros = numeros;
	}

}
