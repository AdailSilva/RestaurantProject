package com.adailsilva.service;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@XStreamAlias("webservicecep")
public class CepServiceVO {

	@XStreamAlias("uf")
	@Getter
	@Setter
	private String uf;

	@XStreamAlias("cidade")
	@Getter
	@Setter
	private String cidade;

	@XStreamAlias("bairro")
	@Getter
	@Setter
	private String bairro;

	@XStreamAlias("tipo_logradouro")
	@Getter
	@Setter
	private String tipo_logradouro;

	@XStreamAlias("logradouro")
	@Getter
	@Setter
	private String logradouro;

	@XStreamAlias("resultado")
	@Getter
	@Setter
	private String resultado;

	@XStreamAlias("resultado_txt")
	@Getter
	@Setter
	private String resultado_txt;

	@Override
	public String toString() {
		return "CepServiceVO [uf=" + uf + ", cidade=" + cidade + ", bairro=" + bairro + ", tipo_logradouro="
				+ tipo_logradouro + ", logradouro=" + logradouro + ", resultado=" + resultado + ", resultado_txt="
				+ resultado_txt + "]";
	}
	
	

}
