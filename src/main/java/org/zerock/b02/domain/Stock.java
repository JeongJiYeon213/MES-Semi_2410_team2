package org.zerock.b02.domain;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product") // Lazy Loading에서 무한 루프 방지
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long stockId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private Long currentStock;

	@Formula("(SELECT p.product_code FROM product p WHERE p.product_id = product_id)")
	private String productCode;

	public void change(Long currentStock) {
		if (currentStock == null || currentStock < 0) {
			throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
		}
		this.currentStock = currentStock;
	}
}
