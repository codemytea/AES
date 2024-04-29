package com.aes.expertsystem.Data.Buying.services

import com.aes.expertsystem.Data.Buying.entities.SIDSeedDataFull
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object RawRequestService {
    private const val URL = "https://fyxheguykvewpdeysvoh.supabase.co"

    fun EPSummary(startLetters: String) =
        "/rest/v1/species_summary?select=*&or=%28has_germination.eq.true%2Chas_oil.eq.true%2Chas_protein.eq.true%2Chas_dispersal.eq.true%2Chas_seed_weights.eq.true%2Chas_storage_behaviour.eq.true%2Chas_morphology.eq.true%29&genus=ilike.$startLetters%25&order=genus.asc.nullslast%2Cepithet.asc.nullslast"

    fun EPFull(id: String) =
        "/rest/v1/species?select=*,family(name),germination(percent_germ,presow_treatment,temperature,light_hours,days,medium,provenance,sample_size,reference_id),dispersal(notes,method,animal_group,animal_species,dispersal_agents(description),reference_id),storage_behaviour(*),seed_weights(thousandseedweight,notes,material_weighed(description),reference_id),protein_content(protein_content,moisture_status,notes,material_weighed(description),reference_id),oil_content(oil_content,moisture_status,notes,material_weighed(description),reference_id),morphology(*)&id=eq.$id"

    val requestProps =
        mapOf(
            "X-Client-Info" to "supabase-js/1.35.7",
            "Sec-Ch-Ua" to """""Not(A:Brand";v="24", "Chromium";v="122"""",
            "Sec-Ch-Ua-Mobile" to "?0",
            "Authorization" to "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ5eGhlZ3V5a3Zld3BkZXlzdm9oIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NDc0MTY1MzQsImV4cCI6MTk2Mjk5MjUzNH0.XhJKVijhMUidqeTbH62zQ6r8cS6j22TYAKfbbRHMTZ8",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.6261.95 Safari/537.36",
            "Accept-Profile" to "public",
            "Apikey" to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ5eGhlZ3V5a3Zld3BkZXlzdm9oIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NDc0MTY1MzQsImV4cCI6MTk2Mjk5MjUzNH0.XhJKVijhMUidqeTbH62zQ6r8cS6j22TYAKfbbRHMTZ8",
            "Sec-Ch-Ua-Platform" to "macOS",
            "Accept" to "*/*",
            "Origin" to "https://ser-sid.org",
            "Sec-Fetch-Site" to "cross-site",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Dest" to "empty",
            "Referer" to "https://ser-sid.org/",
            "Accept-Encoding" to "application/json",
            "Accept-Language" to "en-GB,en-US;q=0.9,en;q=0.8",
            "Priority" to "u=1, i",
        )

    @JsonIgnoreProperties(ignoreUnknown = true)
    class SIDSeedData(
        val genus: String = "",
        val epithet: String = "",
        val id: String = "",
        val infraspecies_rank: String? = null,
        val infraspecies_epithet: String? = null,
        val has_germination: Boolean = false,
        val has_oil: Boolean = false,
        val has_protein: Boolean = false,
        val has_dispersal: Boolean = false,
        val has_seed_weights: Boolean = false,
        val has_storage_behaviour: Boolean = false,
        val has_morphology: Boolean = false,
    )

    fun setGETForFull(data: SIDSeedData): SIDSeedDataFull? {
        val obj = URL(URL + EPFull(data.id))
        val con = obj.openConnection() as HttpURLConnection

        con.setRequestMethod("GET")
        requestProps.forEach {
            con.setRequestProperty(it.key, it.value)
        }

        val responseCode = con.getResponseCode()

        return if (responseCode == HttpURLConnection.HTTP_OK) { // success
            val `in` = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val response = StringBuffer()

            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()

            val mapper = jacksonObjectMapper()
            val typeRef = mapper.typeFactory.constructCollectionType(List::class.java, SIDSeedDataFull::class.java)
            jacksonObjectMapper().readValue<List<SIDSeedDataFull>>(response.toString(), typeRef).firstOrNull()
        } else {
            // GET request did not work.
            null
        }
    }

    fun sendGETForIds(startLetters: String): List<SIDSeedData>? {
        val obj = URL(URL + EPSummary(startLetters))
        val con = obj.openConnection() as HttpURLConnection

        con.setRequestMethod("GET")
        requestProps.forEach {
            con.setRequestProperty(it.key, it.value)
        }

        val responseCode = con.getResponseCode()

        return if (responseCode == HttpURLConnection.HTTP_OK) { // success
            val `in` = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val response = StringBuffer()

            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()

            val mapper = jacksonObjectMapper()
            val typeRef = mapper.typeFactory.constructCollectionType(List::class.java, SIDSeedData::class.java)
            jacksonObjectMapper().readValue(response.toString(), typeRef)
        } else {
            // GET request did not work.
            null
        }
    }
}
