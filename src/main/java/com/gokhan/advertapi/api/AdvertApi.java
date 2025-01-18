package com.gokhan.advertapi.api;

import static com.gokhan.advertapi.constants.ApplicationConstant.X_CORRELATION_ID;

import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.response.AdvertErrorResponse;
import com.gokhan.advertapi.api.model.response.AdvertStatusChangeResponse;
import com.gokhan.advertapi.api.model.response.AdvertStatusStatisticsResponse;
import com.gokhan.advertapi.constants.AdvertApiExampleConstants;
import com.gokhan.advertapi.constants.ApplicationConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@Tag(name = "Advert", description = "This API is used to manage advert.")
@RequestMapping(ApplicationConstant.ADVERT_REQUEST_URL)
public interface AdvertApi {

  @Operation(summary = "Creates advert.")
  @ApiResponse(responseCode = "201", description = "Advert created")
  @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.BAD_REQUEST)}))
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.INTERNAL_ERROR)}))
  @ApiResponse(responseCode = "503", description = "Service Unavailable",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.SERVICE_UNAVAILABLE)}))
  @Parameter(name = X_CORRELATION_ID, description =
      " An unique identifier value "
          + "that is attached to requests and messages that allow reference to a particular "
          + "transaction or event chain", in = ParameterIn.HEADER,
      example = "c2abf341-6858-42f0-8f0b-c2a8ed7cff65")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Void> createAdvert(
      final @RequestHeader(value = X_CORRELATION_ID, required = false) String correlationId,
      final @Valid @RequestBody AdvertRequest request);

  @Operation(summary = "Updates the status of an advert.")
  @ApiResponse(responseCode = "200", description = "Advert status updated")
  @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.BAD_REQUEST)}))
  @ApiResponse(responseCode = "404", description = "Advert not found",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.NOT_FOUND)}))
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.INTERNAL_ERROR)}))
  @ApiResponse(responseCode = "503", description = "Service Unavailable",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.SERVICE_UNAVAILABLE)}))
  @Parameter(name = X_CORRELATION_ID, description =
      "An unique identifier value that is attached to requests and messages that allow reference to a particular "
          + "transaction or event chain", in = ParameterIn.HEADER,
      example = "c2abf341-6858-42f0-8f0b-c2a8ed7cff65")
  @PatchMapping(value = "/advert-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Void> updateAdvertStatus(
      final @RequestHeader(value = X_CORRELATION_ID, required = false) String correlationId,
      final @Valid @RequestBody AdvertStatusUpdateRequest request);

  @Operation(summary = "Lists statistics for adverts by their status.")
  @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.INTERNAL_ERROR)}))
  @ApiResponse(responseCode = "503", description = "Service Unavailable",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.SERVICE_UNAVAILABLE)}))
  @Parameter(name = X_CORRELATION_ID, description =
      "An unique identifier value attached to requests for tracking purposes",
      in = ParameterIn.HEADER, example = "c2abf341-6858-42f0-8f0b-c2a8ed7cff65")
  @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AdvertStatusStatisticsResponse> getAdvertStatusStatistics(
      @RequestHeader(value = X_CORRELATION_ID, required = false) String correlationId);

  @Operation(summary = "Lists all status changes for a specific advert.")
  @ApiResponse(responseCode = "200", description = "Status changes retrieved successfully",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Advert not found",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.NOT_FOUND)}))
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.INTERNAL_ERROR)}))
  @ApiResponse(responseCode = "503", description = "Service Unavailable",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AdvertErrorResponse.class),
          examples = {@ExampleObject(value = AdvertApiExampleConstants.SERVICE_UNAVAILABLE)}))
  @Parameter(name = X_CORRELATION_ID, description =
      "An unique identifier value attached to requests for tracking purposes",
      in = ParameterIn.HEADER, example = "c2abf341-6858-42f0-8f0b-c2a8ed7cff65")
  @Parameter(name = "advertId", description = "The ID of the advert", required = true,
      in = ParameterIn.PATH, example = "1")
  @GetMapping(value = "/{advertId}/status-changes", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<AdvertStatusChangeResponse> getAdvertStatusChanges(
      @RequestHeader(value = X_CORRELATION_ID, required = false) String correlationId,
      @PathVariable("advertId") Long advertId);
}
