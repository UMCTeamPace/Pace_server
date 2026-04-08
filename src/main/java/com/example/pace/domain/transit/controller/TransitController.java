package com.example.pace.domain.transit.controller;

import com.example.pace.domain.transit.controller.docs.TransitControllerDocs;
import com.example.pace.domain.transit.dto.request.BusInfoReqDTO;
import com.example.pace.domain.transit.dto.request.SubwayArrivalReqDTO;
import com.example.pace.domain.transit.dto.response.BusInfoResDTO;
import com.example.pace.domain.transit.dto.response.SubwayArrivalResDTO.SubwayArrivalInfoDTO;
import com.example.pace.domain.transit.exception.code.SubwayApiSuccessCode;
import com.example.pace.domain.transit.exception.code.TransitSuccessCode;
import com.example.pace.domain.transit.service.BusNetworkService;
import com.example.pace.domain.transit.service.query.SubwayApiQueryService;
import com.example.pace.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transit")
public class TransitController implements TransitControllerDocs {
    private final SubwayApiQueryService subwayApiQueryService;
    private final BusNetworkService busNetworkService;

    @GetMapping("/arrivals")
    public ApiResponse<List<SubwayArrivalInfoDTO>> getArrivals(
            @ModelAttribute SubwayArrivalReqDTO.SubwayArrivalDTO request
    ) {
        return ApiResponse.onSuccess(
                SubwayApiSuccessCode.SUBWAY_API_SUCCESS,
                subwayApiQueryService.getLiveSubwayStation(request)
        );
    }

    @GetMapping("/bus/start-station")
    public ApiResponse<BusInfoResDTO.BusInfoDTO> searchBusStartStation(
            @Valid @ModelAttribute BusInfoReqDTO.BusStartStationDTO request
    ) {
        return ApiResponse.onSuccess(
                TransitSuccessCode.TRANSIT_BUS_OK,
                busNetworkService.searchStartStationInfo(
                        request.getLineName(),
                        request.getStartStation(),
                        request.getEndStation()
                )
        );
    }
}
