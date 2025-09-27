package room;

import java.util.ArrayList;
import java.util.Collection;

import gameclient.rooms.RoomConfig;
import gameclient.rooms.RoomInfo;
import gameclient.user.IUserView;

public record Room(int roomID, Collection<RoomMember> members, Admin admin, RoomConfig roomConfig) {

    public Room(RoomMember admin, RoomConfig roomConfig, int roomID) {
        this(roomID, new ArrayList<>(), new Admin(admin.userView()), roomConfig);
        this.members().add(admin);
    }

    public RoomInfo getRoomInfo() {
        return new RoomInfo(
                roomID,
                roomConfig.name(),
                roomConfig.isPublic(),
                members.size(),
                roomConfig.maxPlayers(),
                hasPassword());
    }

    public boolean hasPassword() {
        return roomConfig.password() != null;
    }

    public synchronized void setAdmin(RoomMember newAdmin) {
        if (members.contains(newAdmin)) {
            admin.changeAdmin(newAdmin.userView());
        }
        notifyMembers();
    }

    public boolean isAdmin(RoomMember user) {
        return admin.admin().equals(user.userView());
    }

    public synchronized void addMember(RoomMember member) {
        if (members.size() < roomConfig.maxPlayers()) {
            members.add(member);
            member.roomsUserHandle().joinRoomCommand(this);
        }
        notifyMembers();
    }

    public synchronized void removeMember(RoomMember member) {
        if (members.remove(member)) {
            member.roomsUserHandle().leaveRoomCommand();
            if (members.isEmpty())
                return;
            if (isAdmin(member)) {
                admin.changeAdmin(members.iterator().next().userView());
            }
        }
        notifyMembers();
    }

    public synchronized void removeAllMembers() {
        for (RoomMember member : members) {
            member.roomsUserHandle().leaveRoomCommand();
        }
        notifyMembers();
        members.clear();
    }

    public boolean isFull() {
        return members.size() == roomConfig.maxPlayers();
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public static class Admin {
        private IUserView admin;

        public Admin(IUserView admin) {
            this.admin = admin;
        }

        public IUserView admin() {
            return admin;
        }

        public void changeAdmin(IUserView admin) {
            this.admin = admin;
        }
    }

    private void notifyMembers() {
        for (RoomMember member : members) {
            member.roomsUserHandle().notifyRoomChange();
        }
    }
}
