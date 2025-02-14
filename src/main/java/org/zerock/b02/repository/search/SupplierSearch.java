package org.zerock.b02.repository.search;

import org.zerock.b02.domain.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierSearch {

    Page<Supplier> searchAll(String[] types, String keyword, Pageable pageable);
}
