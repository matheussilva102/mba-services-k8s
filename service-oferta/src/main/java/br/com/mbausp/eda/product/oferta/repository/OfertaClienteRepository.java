package br.com.mbausp.eda.product.oferta.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.mbausp.eda.product.oferta.entity.OfertaClienteEntity;

public interface OfertaClienteRepository extends JpaRepository<OfertaClienteEntity, Long> {

	Optional<List<OfertaClienteEntity>> findByClienteId(String clientId);

	@Query(nativeQuery = true, value = """
			WITH sqlInsert AS (
			    INSERT INTO oferta.oferta_cliente(
					cliente_id, oferta_id, oferta_ativa, data_criacao, data_expiracao, status, origem_oferta)
					VALUES (:cliente_id, :oferta_id, :oferta_ativa, :data_criacao, :data_expiracao, :status, :origem_oferta)
					ON CONFLICT (cliente_id, oferta_id) DO NOTHING RETURNING *
			)
			SELECT * FROM sqlInsert
			UNION ALL
			SELECT * FROM oferta.oferta_cliente WHERE cliente_id = :cliente_id AND oferta_id = :oferta_id
			LIMIT 1
			""")
	OfertaClienteEntity saveConditionally(
			@Param("cliente_id") String clientId,
			@Param("oferta_id") Integer ofertaId,
			@Param("oferta_ativa") boolean ofertaAtiva,
			@Param("data_criacao") LocalDateTime dataCriacao,
			@Param("data_expiracao") LocalDateTime dataExpiracao,
			@Param("status") String status,
			@Param("origem_oferta") Integer origemOferta);

}
