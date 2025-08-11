package room;

import user.IMatchmakingUserHandle;
import user.UserInfo;

import java.util.ArrayList;
import java.util.Collection;

public record Room(Collection<RoomMember> members, Admin admin, RoomConfig roomConfig) {

    public Room(RoomMember admin, RoomConfig roomConfig) {
        this(new ArrayList<>(), new Admin(admin.userInfo()), roomConfig);
        this.members().add(admin);
    }

    public RoomInfo getRoomInfo() {
        return new RoomInfo(
                roomConfig.name(),
                roomConfig.isPublic(),
                members.size(),
                roomConfig.maxPlayers(),
                hasPassword()
        );
    }

    public boolean hasPassword() {
        return roomConfig.password() != null;
    }

    public void setAdmin(RoomMember newAdmin) {
        if (members.contains(newAdmin)) {
            admin.changeAdmin(newAdmin.userInfo());
        }
    }

    public boolean isAdmin(RoomMember user) {
        return admin.admin().equals(user.userInfo());
    }

    public void addMember(RoomMember member) {
        if (members.size() < roomConfig.maxPlayers()) {
            members.add(member);
            member.roomsUserHandle().joinRoomCommand(this);
        }
    }

    public void removeMember(RoomMember member) {
        if (members.remove(member)) {
            member.roomsUserHandle().leaveRoomCommand();
            if (members.isEmpty()) return;
            if (isAdmin(member)) {
                admin.changeAdmin(members.iterator().next().userInfo());
            }
        }
    }

    public void removeAllMembers() {
        for (RoomMember member : members) {
            member.roomsUserHandle().leaveRoomCommand();
        }
        members.clear();
    }

    public boolean isFull() {
        return members.size() == roomConfig.maxPlayers();
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public static class Admin {
        private UserInfo admin;

        public Admin(UserInfo admin) {
            this.admin = admin;
        }

        public UserInfo admin() {
            return admin;
        }

        public void changeAdmin(UserInfo admin) {
            this.admin = admin;
        }
    }
}
