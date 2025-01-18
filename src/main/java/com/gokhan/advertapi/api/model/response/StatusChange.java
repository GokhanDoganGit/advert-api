package com.gokhan.advertapi.api.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusChange {

  private String oldStatus;

  private String newStatus;

  @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
  private LocalDateTime changeDateTime;
}
