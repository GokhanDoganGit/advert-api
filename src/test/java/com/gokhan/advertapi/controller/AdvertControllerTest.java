package com.gokhan.advertapi.controller;

import static com.gokhan.advertapi.common.AdvertUtil.createAdvertRequest;
import static com.gokhan.advertapi.constant.TestConstant.ADVERT_ID;
import static com.gokhan.advertapi.constants.ApplicationConstant.ADVERT_REQUEST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.request.Status;
import com.gokhan.advertapi.service.AdvertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest
public class AdvertControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AdvertService advertService;

  @Test
  void testCreateAdvert() throws Exception {
    // When
    AdvertRequest advertRequest = createAdvertRequest();

    // Then
    mockMvc.perform(MockMvcRequestBuilders.post(ADVERT_REQUEST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(advertRequest)))
        .andExpect(status().isCreated());

  }

  @Test
  void testUpdateAdvertStatus() throws Exception {
    AdvertStatusUpdateRequest statusUpdateRequest = new AdvertStatusUpdateRequest();
    statusUpdateRequest.setAdvertId(ADVERT_ID);
    statusUpdateRequest.setNewStatus(Status.DEAKTIF);

    mockMvc.perform(
            MockMvcRequestBuilders.patch(ADVERT_REQUEST_URL + "/advert-status", ADVERT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdateRequest)))
        .andExpect(status().isOk());
  }

  @Test
  void testGetAdvertStatusStatistics() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(ADVERT_REQUEST_URL + "/statistics")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testGetAdvertStatusChanges() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.get(ADVERT_REQUEST_URL + "/{advertId}/status-changes",
                    ADVERT_ID)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void testBadRequestWhenInvalidInput() throws Exception {
    AdvertStatusUpdateRequest invalidRequest = new AdvertStatusUpdateRequest();
    invalidRequest.setNewStatus(null);
    mockMvc.perform(
            MockMvcRequestBuilders.patch(ADVERT_REQUEST_URL + "/advert-status", ADVERT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Request parameters didn't validate."));
  }

}