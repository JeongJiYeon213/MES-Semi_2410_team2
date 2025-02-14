package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    private String link;
    private String type;
    private String keyword;

    // 드롭다운 검색 기능 추가된 필드
    private String customerId;

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
            if(keyword != null) {
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
    public void setSize(int size) {
        if (size <= 0) {
            this.size = 10;
        } else {
            this.size = size;
        }
    }
}
