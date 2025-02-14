package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InboundTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long inboundId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id", nullable = false)
	private Stock stock;

	@Column(nullable = false)
	private Long plus; // 입고 수량
}
