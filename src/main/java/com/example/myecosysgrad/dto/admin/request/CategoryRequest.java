package com.example.myecosysgrad.dto.admin.request;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    String name;
    String photoUrl;
    String slug;
    Boolean active;
    Integer parentId;
    Set<Integer> brandIds; // logic phù hợp hơn: chỉ truyền id brand, liên kết sẽ xử lý ở service
}

