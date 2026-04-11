package br.com.mbausp.eda.product.auto.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.mbausp.eda.product.auto.entity.AutoClienteEntity;

public interface AutoClienteRepository extends JpaRepository<AutoClienteEntity, Long> {

	Optional<List<AutoClienteEntity>> findByClienteId(String clientId);

	@Query(nativeQuery = true, value = """
			WITH sqlInsert AS (
			    INSERT INTO auto.auto_cliente(
					cliente_id, nu_contrato, status, data_criacao)
					VALUES (:cliente_id, :nu_contrato, :status, :data_criacao)
					ON CONFLICT (cliente_id, nu_contrato) DO NOTHING RETURNING *
			)
			SELECT * FROM sqlInsert
			UNION ALL
			SELECT * FROM auto.auto_cliente WHERE cliente_id = :cliente_id AND nu_contrato = :nu_contrato
			LIMIT 1
			""")
	AutoClienteEntity saveConditionally(
			@Param("cliente_id") String clientId,
			@Param("nu_contrato") Integer contract,
			@Param("status") String status,
			@Param("data_criacao") LocalDateTime dataCriacao);
}
