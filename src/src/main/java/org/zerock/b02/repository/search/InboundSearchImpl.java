package org.zerock.b02.repository.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Inbound;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InboundSearchImpl implements InboundSearch {
    private final EntityManager entityManager;

    @Override
    public Page<Inbound> searchAll(String[] types, String keyword, Pageable pageable) {
        // JPQL로 작성
        StringBuilder jpql = new StringBuilder("SELECT i FROM Inbound i WHERE i.inboundId > 0");

        // 동적 검색 조건 추가
        if ((types != null && types.length > 0) && keyword != null) {
            jpql.append(" AND (");

            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                switch (type) {
                    case "a":
                        jpql.append("i.inboundCode LIKE :keyword");
                        break;
                    case "b":
                        jpql.append("i.product.productCode LIKE :keyword");
                        break;
                    case "c":
                        jpql.append("i.supplier.supplierId LIKE :keyword");
                        break;
                    case "d":
                        jpql.append("i.inboundStatus LIKE :keyword");
                        break;
                }

                // 각 조건 사이에 "OR" 추가
                if (i < types.length - 1) {
                    jpql.append(" OR ");
                }
            }
            jpql.append(")");
        }

        // JPQL로 쿼리 생성
        TypedQuery<Inbound> query = entityManager.createQuery(jpql.toString(), Inbound.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                jpql.toString().replace("SELECT i", "SELECT COUNT(i)"), Long.class
        );

        // 파라미터 바인딩
        if ((types != null && types.length > 0) && keyword != null) {
            query.setParameter("keyword", "%" + keyword + "%");
            countQuery.setParameter("keyword", "%" + keyword + "%");
        }

        // 페이징 처리
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // 결과 조회
        List<Inbound> list = query.getResultList();
        long count = countQuery.getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }
}
