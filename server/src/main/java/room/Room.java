package room;

import user.UserHandle;

import java.util.ArrayList;
import java.util.List;

public record Room(String name, List<UserHandle> members, Admin admin) {

    public Room(String name, UserHandle adminHandle) {
        this(name, new ArrayList<>(), new Admin(adminHandle));
        this.members().add(adminHandle);
    }

    public UserHandle getAdmin() {
        return admin.admin();
    }

    public void setAdmin(UserHandle newAdmin) {
        if (!members.contains(newAdmin)) {
            throw new IllegalArgumentException("User is not a member of the room");
        }
        admin.changeAdmin(newAdmin);
    }

    public void addMember(UserHandle member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    public void removeMember(UserHandle member) {
        if (members.remove(member)) {
            if (members.isEmpty()) {
                return;
            }
            if (admin.admin().equals(member)) {
                admin.changeAdmin(members().get(0));
            }
        }
    }

    public static class Admin {
        private UserHandle admin;

        public Admin(UserHandle admin) {
            this.admin = admin;
        }

        public UserHandle admin() {
            return admin;
        }

        public void changeAdmin(UserHandle admin) {
            this.admin = admin;
        }
    }
}
