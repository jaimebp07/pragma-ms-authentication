package co.com.mycompany.api.dto;

import java.util.Set;
import java.util.UUID;

public record GetListUsersRqDTO(
    Set<UUID> listUserIds
) { }
