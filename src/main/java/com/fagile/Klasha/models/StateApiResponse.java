package com.fagile.Klasha.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class StateApiResponse {
    private boolean error;
    private String msg;
    private StateData data;

}
