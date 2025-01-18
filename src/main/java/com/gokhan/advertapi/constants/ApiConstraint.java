package com.gokhan.advertapi.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstraint {

  public static final String TITLE = "^[a-zA-Z0-9İıŞşÇçÖöÜüĞğ].*";

}
