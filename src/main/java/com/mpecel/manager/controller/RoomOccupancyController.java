package com.mpecel.manager.controller;

import com.mpecel.manager.dto.RoomOccupancyDto;
import com.mpecel.manager.service.RoomOccupancyService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/occupancy")
@Validated
public class RoomOccupancyController {

    private final RoomOccupancyService roomOccupancyService;

    @Autowired
    public RoomOccupancyController(RoomOccupancyService roomOccupancyService) {
        this.roomOccupancyService = roomOccupancyService;
    }

    @PostMapping("/calculate")
    public RoomOccupancyDto calculateOccupancy(
            @RequestParam @Min(0) int freePremiumRooms,
            @RequestParam @Min(0) int freeEconomyRooms) {
        return roomOccupancyService.calculateOccupancy(freePremiumRooms, freeEconomyRooms);
    }
}
