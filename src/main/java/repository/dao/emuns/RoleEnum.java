package repository.dao.emuns;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum RoleEnum {
    User("ROLE_USER"),
    Admin("ROLE_ADMIN"),
    Unconfirmed("ROLE_UNCONFIRMED");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static RoleEnum byName(String name){
        switch (name){
            case "ROLE_USER" : return User;
            case "ROLE_UNCONFIRMED" : return Unconfirmed;
            case "ROLE_ADMIN" : return Admin;
            default : return null;
        }
    }

    public static List<String> getAllRoles(){
        return Arrays.stream(RoleEnum.values()).map(Objects::toString).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(RoleEnum.valueOf("User").getName());
    }

}
