package net.focik.homeoffice.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookstoreDto {
    private Integer id;
    private String name;
    private String url;
}
