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
public class OutboundTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long outboundId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id", nullable = false)
	private Stock stock;

	@Column(nullable = false)
	private Long minus; // 입고 수량
}
