package com.infoepoch.cmgs.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryEvent extends Event {

    private String username;

    private String password;
}
