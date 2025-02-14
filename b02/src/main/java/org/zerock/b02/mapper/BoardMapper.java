package org.zerock.b02.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zerock.b02.domain.Admin;


@Mapper
public interface BoardMapper {
    Long insert(Admin admin);
}
