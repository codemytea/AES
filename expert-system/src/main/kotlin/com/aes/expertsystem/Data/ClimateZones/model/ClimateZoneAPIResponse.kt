package com.aes.expertsystem.Data.ClimateZones.model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ClimateZoneAPIResponse(
    @JsonAlias("return_values") val returnValues: List<ClimateZoneAPIReturnValues>,
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ClimateZoneAPIReturnValues(
        @JsonAlias("koppen_geiger_zone") val koppenClimateZone: KoppenClimateZone,
    )
}
