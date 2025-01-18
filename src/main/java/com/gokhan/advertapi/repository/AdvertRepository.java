package com.gokhan.advertapi.repository;

import com.gokhan.advertapi.api.model.request.Category;
import com.gokhan.advertapi.repository.entity.Advert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

  List<Advert> findAllByTitleAndDescriptionAndCategory(String title, String description,
      Category category);

}
