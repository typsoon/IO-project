package room;

import user.IUserHandle;

import java.util.ArrayList;
import java.util.Collection;

public record Room(Collection<RoomMember> members, Admin admin, RoomConfig roomConfig) {

    public Room(RoomMember admin, RoomConfig roomConfig) {
        this(new ArrayList<>(), new Admin(admin.user()), roomConfig);
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

    public IUserHandle getAdmin() {
        return admin.admin();
    }

    public void setAdmin(RoomMember newAdmin) {
        if (members.contains(newAdmin)) {
            admin.changeAdmin(newAdmin.user());
        }
    }

    public void addMember(RoomMember member) {
        if (members.size() < roomConfig.maxPlayers()) {
            members.add(member);
        }
        else throw new IllegalStateException("Room is full"); // TODO: Handle this case properly
    }

    public void removeMember(RoomMember member) {
        if (members.remove(member)) {
            member.userHandle().leaveRoom();
            if (members.isEmpty()) {
                return;
            }
            if (admin.admin().equals(member.user())) {
                admin.changeAdmin(members.iterator().next().user());
            }
        }
    }

    public static class Admin {
        private IUserHandle admin;

        public Admin(IUserHandle admin) {
            this.admin = admin;
        }

        public IUserHandle admin() {
            return admin;
        }

        public void changeAdmin(IUserHandle admin) {
            this.admin = admin;
        }
    }
}
