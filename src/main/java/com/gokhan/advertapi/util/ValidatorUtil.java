package com.gokhan.advertapi.util;

import static com.gokhan.advertapi.constants.ApplicationConstant.AKTIF;
import static com.gokhan.advertapi.constants.ApplicationConstant.DEAKTIF;
import static com.gokhan.advertapi.constants.ApplicationConstant.MUKERRER;
import static com.gokhan.advertapi.constants.ApplicationConstant.ONAY_BEKLIYOR;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidatorUtil {

  public static void validateStatusTransition(String currentStatus, String newStatus) {
    if (currentStatus.equals(newStatus)) {
      throw new IllegalStateException(
          "The status to be updated is the same as the current status.");
    }
    if (MUKERRER.equals(currentStatus)) {
      throw new IllegalStateException("The status of duplicate adverts cannot be updated.");
    }
    if (MUKERRER.equals(newStatus)) {
      throw new IllegalStateException("The status of adverts cannot be updated to duplicate.");
    }
    if (ONAY_BEKLIYOR.equals(newStatus) && AKTIF.equals(currentStatus)) {
      throw new IllegalStateException(
          "The active status of adverts cannot be updated to pending approval.");
    }
    if (DEAKTIF.equals(newStatus)) {
      if (!AKTIF.equals(currentStatus) && !ONAY_BEKLIYOR.equals(currentStatus)) {
        throw new IllegalStateException(
            "The transition to the Deactivated status can only be made from the Active or Pending Approval status.");
      }
    }
    if (AKTIF.equals(newStatus) && !ONAY_BEKLIYOR.equals(currentStatus)) {
      throw new IllegalStateException(
          "Switching to Active Status can only be done from the Pending Approval status.");
    }
  }
}
