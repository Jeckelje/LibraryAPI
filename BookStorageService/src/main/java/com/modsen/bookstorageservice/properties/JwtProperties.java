package com.modsen.bookstorageservice.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {

    private String secret = "YXN2ZHVneWFoZ3VpYXN2YWRiYWhsaWF2cmJobGlhdnJiaGx2YXNiaGpzYWJobHZhc2Jo";

    private long access = 3600000;

    private long refresh = 259000000;

}
