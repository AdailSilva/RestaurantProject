package com.projeto.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.Restaurante;

@Repository(forEntity = Restaurante.class)
public abstract class RestauranteRepository extends AbstractEntityRepository<Restaurante, Integer> {

}
