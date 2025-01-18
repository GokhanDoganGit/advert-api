package com.gokhan.advertapi.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationConstant {

  public static final String X_CORRELATION_ID = "X-Correlation-ID";
  public static final String CORRELATION_ID = "correlationId";
  public static final String ADVERT_REQUEST_URL = "/api/advert";

  public static final String DEAKTIF = "DEAKTIF";
  public static final String AKTIF = "AKTIF";
  public static final String ONAY_BEKLIYOR = "ONAY_BEKLIYOR";
  public static final String MUKERRER = "MUKERRER";
}
