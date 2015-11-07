package com.silverforge.elasticsearchrawclient.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoPoint {
    private float longitude;
    private float latitude;
}
