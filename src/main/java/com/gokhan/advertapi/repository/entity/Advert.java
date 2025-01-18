package com.gokhan.advertapi.repository.entity;

import com.gokhan.advertapi.api.model.request.Category;
import com.gokhan.advertapi.api.model.request.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "statusHistory")
@Table(name = "ADVERT")
public class Advert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "TITLE", length = 50, nullable = false)
  private String title;

  @Column(name = "DESCRIPTION", length = 200, nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", nullable = false)
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  private Status status;

  @Column(name = "CREATED_DATE", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "EXPIRY_DATE")
  private LocalDateTime expiryDate;

  @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
  private List<AdvertStatusChange> statusHistory = new ArrayList<>();

  public List<AdvertStatusChange> getStatusHistory() {
    if (statusHistory == null) {
      statusHistory = new ArrayList<>();
    }
    return statusHistory;
  }
}
