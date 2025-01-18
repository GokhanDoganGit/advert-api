package com.gokhan.advertapi.repository.entity;

import com.gokhan.advertapi.api.model.request.Status;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class AdvertStatusChange {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "advert_id")
  private Advert advert;

  @Enumerated(EnumType.STRING)
  private Status oldStatus;

  @Enumerated(EnumType.STRING)
  private Status newStatus;

  private LocalDateTime changeTime;
}
