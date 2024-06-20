package com.mpecel.manager.service;

import com.mpecel.manager.dto.RoomOccupancyDto;
import com.mpecel.manager.repository.GuestPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoomOccupancyService {
    private final GuestPaymentRepository guestPaymentRepository;
    private static final double PREMIUM_THRESHOLD = 100.0;

    @Autowired
    public RoomOccupancyService(GuestPaymentRepository guestPaymentRepository) {
        this.guestPaymentRepository = guestPaymentRepository;
    }

    public static class Result {
        public int premiumRoomsUsed;
        public double premiumRevenue;
        public int economyRoomsUsed;
        public double economyRevenue;

        public Result(int premiumRoomsUsed, double premiumRevenue, int economyRoomsUsed, double economyRevenue) {
            this.premiumRoomsUsed = premiumRoomsUsed;
            this.premiumRevenue = premiumRevenue;
            this.economyRoomsUsed = economyRoomsUsed;
            this.economyRevenue = economyRevenue;
        }
    }

    public RoomOccupancyDto calculateOccupancy(int freePremiumRooms, int freeEconomyRooms) {
        List<Double> reverseSortedPayments = guestPaymentRepository.getGuestPayments()
                .stream()
                .sorted(Collections.reverseOrder())
                .toList();

        // premium
        List<Double> reverseSortedPremiumPayments = reverseSortedPayments.stream()
                .filter(payment -> payment >= PREMIUM_THRESHOLD)
                .toList();
        List<Double> premiumRoomAllocations = reverseSortedPremiumPayments.stream()
                .limit(freePremiumRooms)
                .toList();
        double premiumRevenue = premiumRoomAllocations.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        int premiumRoomsUsed = premiumRoomAllocations.size();

        // economy
        int remainingPremiumRooms = freePremiumRooms - premiumRoomsUsed;
        List<Double> reverseSortedEconomyPayments = reverseSortedPayments.stream()
                .filter(payment -> payment < PREMIUM_THRESHOLD)
                .toList();
        int totalEconomyRooms = freeEconomyRooms + remainingPremiumRooms;
        List<Double> economyRoomAllocations = reverseSortedEconomyPayments.stream()
                .limit(totalEconomyRooms)
                .toList();
        double economyRevenue = economyRoomAllocations.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        int economyRoomsUsed = Math.min(freeEconomyRooms, economyRoomAllocations.size());

        // adjusting for upgrades
        int upgrades = Math.max(0, economyRoomAllocations.size() - freeEconomyRooms);
        premiumRoomsUsed += upgrades;
        double upgradedRevenue = economyRoomAllocations.stream()
                .limit(upgrades)
                .mapToDouble(Double::doubleValue)
                .sum();
        premiumRevenue += upgradedRevenue;
        economyRevenue -= upgradedRevenue;

        return new RoomOccupancyDto(premiumRoomsUsed, economyRoomsUsed, premiumRevenue, economyRevenue);
    }
}
