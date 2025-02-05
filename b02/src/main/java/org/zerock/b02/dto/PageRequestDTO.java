package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;   // 보여줄 page
    @Builder.Default
    private int size = 10;  // 보여줄 page size

    private String link; // page size 같이

    // 검색/필터링 요청 속성들
    private String type;   // 검색종류(t, c, w. tc, tw, twc)
    private String keyword;   // 검색할 문자들

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String...props) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    public String getLink() {
        if(link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0) {
                    builder.append("&type=" + type);
            }
            if(keyword != null) {			// 할일에 대한 제목과 작성자의 문자 링크값
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));  // 한글처리를 위한 코드
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            link = builder.toString();
        }

        return link;
    }
}