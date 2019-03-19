package com.jzy.game.bydr.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import com.jzy.game.bydr.struct.Fish;
import com.jzy.game.bydr.struct.room.Room;
import com.jzy.game.bydr.thread.timer.RoomTimer;
import com.jzy.game.engine.thread.ServerThread;
import com.jzy.game.engine.thread.ThreadType;

/**
 * 房间逻辑线程
 * <p>
 * 一个线程处理多个房间
 * </p>
 * 
 * @author CruiseDing
 * @date 2017-03-24 QQ:359135103
 */
public class RoomThread extends ServerThread {
	private static final AtomicInteger threadNum = new AtomicInteger(0);
	private final List<Room> rooms = new ArrayList<>();
	private RoomTimer roomTimer;

	public RoomThread(ThreadGroup threadGroup, Room room) {
		super(threadGroup, ThreadType.ROOM + "-" + threadNum.getAndIncrement(), 500, 10000); // TODO
        rooms.add(room);
	}

	public RoomTimer getRoomTimer() {
		return roomTimer;
	}

	public void setRoomTimer(RoomTimer roomFishTimer) {
        roomTimer = roomFishTimer;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	

	public Room getRoom(long roomId) {
		Optional<Room> findAny = rooms.stream().filter(r -> r.getId() == roomId).findAny();
		return findAny.orElse(null);
	}

}
