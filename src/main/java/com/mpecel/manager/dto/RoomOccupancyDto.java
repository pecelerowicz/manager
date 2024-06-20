package com.mpecel.manager.dto;

public record RoomOccupancyDto(int premiumRoomsUsed, int economyRoomsUsed,
                               double premiumRevenue, double economyRevenue) {
}
