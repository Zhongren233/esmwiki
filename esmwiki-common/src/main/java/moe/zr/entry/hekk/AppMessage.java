package moe.zr.entry.hekk;

import lombok.Data;

@Data
public class AppMessage {
    private Integer serverVersion;
    private Integer appStatusCode;
    private String appMessage;
    private String currentTime;
    private Integer appHint;
}
