package com.bharat.airport.domain.model;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "airports")
public class Airport {
  @Id
  @NotBlank(message = "Airport code is required")
  @Indexed(unique = true)
  private String code;

  @NotBlank(message = "Airport name is required")
  private String name;

  private boolean isEnabled = true;

  @CreatedDate private LocalDateTime createdAt;

  @CreatedBy private String createdBy;

  @LastModifiedDate private LocalDateTime updatedAt;

  @LastModifiedBy private String updatedBy;
}
