package com.capgemini.hms.room.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * View controller for Room management pages.
 *
 * Client-side JS calls:
 *   GET    /api/v1/rooms?page=&size=&sort=         → list all (paginated)
 *   GET    /api/v1/rooms/{roomNumber}              → single room
 *   POST   /api/v1/rooms                           → create room (ADMIN)
 *   PUT    /api/v1/rooms/{roomNumber}              → update room (ADMIN)
 *   DELETE /api/v1/rooms/{roomNumber}              → soft-delete (ADMIN)
 */
@Controller
@RequestMapping("/room")
public class RoomViewController {

    @GetMapping
    public String listRooms() {
        return "room/list";
    }
}
