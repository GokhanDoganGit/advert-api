package com.gokhan.advertapi.api.model.request;

import static com.gokhan.advertapi.constants.ApiConstraint.TITLE;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdvertRequest {

  @Pattern(regexp = TITLE, message = "The title must start with a letter or number.")
  @Size(min = 10, max = 50, message = "Title must be between 10 and 50 characters.")
  @Schema(example = "Vehicle for Sale")
  private String title;

  @Size(min = 20, max = 200, message = "Description must be between 20 and 200 characters.")
  @Schema(example = "Vehicle with 4 wheels and 4 doors is open for sale")
  private String description;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Category cannot be null")
  @Schema(enumAsRef = true, description = "Allowed values: EMLAK, VASITA, ALISVERIS, DIGER", example = "VASITA")
  private Category category;

}
