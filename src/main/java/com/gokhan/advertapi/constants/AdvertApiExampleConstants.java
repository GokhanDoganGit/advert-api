package com.gokhan.advertapi.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdvertApiExampleConstants {

  public static final String INTERNAL_ERROR = "{\n" +
      "    \"message\": \"" + ErrorMessageConstants.INTERNAL_ERROR_MESSAGE + "\"\n" +
      "}\n";

  public static final String SERVICE_UNAVAILABLE = "{\n" +
      "    \"message\": \"" + ErrorMessageConstants.SERVICE_UNAVAILABLE + "\"\n" +
      "}\n";

  public static final String BAD_REQUEST = "{\n"
      + "    \"message\": \"" + ErrorMessageConstants.BAD_REQUEST_MESSAGE + "\",\n"
      + "    \"details\": [\n"
      + "        {\n"
      + "            \"errorName\": \"id\",\n"
      + "            \"errorReason\": \"must be valid request.\"\n"
      + "        }\n"
      + "    ]\n"
      + "}";

  public static final String NOT_FOUND = "{\n"
      + "\t\"message\": \"" + ErrorMessageConstants.NOT_FOUND + "\",\n"
      + "\t\"details\": [\n"
      + "\t\t{\n"
      + "\t\t\t\"errorName\": \"advert\",\n"
      + "\t\t\t\"errorReason\": \"must be presented for given id.\"\n"
      + "\t\t}\n"
      + "\t]\n"
      + "}";


}
