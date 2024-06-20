package com.mpecel.manager.service;

import com.mpecel.manager.dto.RoomOccupancyDto;
import com.mpecel.manager.repository.GuestPaymentRepository;
import com.mpecel.manager.repository.MockGuestPaymentRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomOccupancyServiceTest {

    @Test
    public void testCalculateOccupancy() {
        //todo: in principle this should be mocked, but I did it like this, because the implementation I have is a mock already (so that the API can work)
        GuestPaymentRepository guestPaymentRepository = new MockGuestPaymentRepository();
        RoomOccupancyService service = new RoomOccupancyService(guestPaymentRepository);

        RoomOccupancyDto occupancy1 = service.calculateOccupancy(3, 3);
        assertEquals(3, occupancy1.premiumRoomsUsed());
        assertEquals(738.0, occupancy1.premiumRevenue(), 0.01);
        assertEquals(3, occupancy1.economyRoomsUsed());
        assertEquals(167.99, occupancy1.economyRevenue(), 0.01);

        RoomOccupancyDto ocupancy2 = service.calculateOccupancy(7, 5);
        assertEquals(6, ocupancy2.premiumRoomsUsed());
        assertEquals(1054.0, ocupancy2.premiumRevenue(), 0.01);
        assertEquals(4, ocupancy2.economyRoomsUsed());
        assertEquals(189.99, ocupancy2.economyRevenue(), 0.01);

        RoomOccupancyDto occupancy3 = service.calculateOccupancy(2, 7);
        assertEquals(2, occupancy3.premiumRoomsUsed());
        assertEquals(583.0, occupancy3.premiumRevenue(), 0.01);
        assertEquals(4, occupancy3.economyRoomsUsed());
        assertEquals(189.99, occupancy3.economyRevenue(), 0.01);

        RoomOccupancyDto occupancy4 = service.calculateOccupancy(7, 1);
        assertEquals(7, occupancy4.premiumRoomsUsed());
        //todo check
        assertEquals(1153.99, occupancy4.premiumRevenue(), 0.01);
        assertEquals(1, occupancy4.economyRoomsUsed());
        //todo check
        assertEquals(45, occupancy4.economyRevenue(), 0.01);
    }
}