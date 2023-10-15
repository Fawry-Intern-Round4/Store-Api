package com.example.storeservice.error;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class IdsError {
    private  String message;
    private  Set<Long> ids;
    public IdsError(String message, Set<Long> ids) {
        this.message = message;
        this.ids = ids;
    }
}
