package br.com.mbausp.eda.product.conta.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.mbausp.eda.product.conta.entity.ContaClienteEntity;

public interface ContaClienteRepository extends JpaRepository<ContaClienteEntity, Long> {

	Optional<List<ContaClienteEntity>> findByClienteId(String clientId);

	@Query(nativeQuery = true, value = """
			WITH sqlInsert AS (
			    INSERT INTO conta.conta_cliente(
					cliente_id, nu_conta, status, data_criacao)
					VALUES (:cliente_id, :nu_conta, :status, :data_criacao)
					ON CONFLICT (cliente_id, nu_conta) DO NOTHING RETURNING *
			)
			SELECT * FROM sqlInsert
			UNION ALL
			SELECT * FROM conta.conta_cliente WHERE cliente_id = :cliente_id AND nu_conta = :nu_conta
			LIMIT 1
			""")
	ContaClienteEntity saveConditionally(
			@Param("cliente_id") String clientId,
			@Param("nu_conta") Long nuConta,
			@Param("status") String status,
			@Param("data_criacao") LocalDateTime dataCriacao);
}
