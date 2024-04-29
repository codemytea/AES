package com.aes.expertsystem.Data.CropGroup.services

import com.aes.expertsystem.Data.CropGroup.entities.CropGroupEntity
import com.aes.expertsystem.Data.CropGroup.entities.CropGroupEntry
import com.aes.expertsystem.Data.CropGroup.repositories.CropGroupEntryRepository
import com.aes.expertsystem.Data.CropGroup.repositories.CropGroupRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

/**
 * Service to extract crop group information from tsv (which was made from an IR-4 pdf) and save to DB
 * */
@Service
open class CropGroupParseService(
    val cropGroupRepository: CropGroupRepository,
    val cropGroupEntryRepository: CropGroupEntryRepository,
) {
    @Transactional
    open fun writeAllToDB() {
        writeDataToDatabase(File(this::class.java.classLoader.getResource("crop_groups.tsv")!!.toURI()))
    }

    private fun parseLineAsSubgroup(
        parts: List<String>,
        dotIndex: Int,
    ): Pair<Triple<String, Int, String>, String> {
        val name =
            parts.first()
                .substring(dotIndex + 1)
                .lowercase()
                .replace("subgroup", "")
                .replace("(cont)", "")
                .trim()

        val letter = parts.first()[dotIndex - 1].toString()
        val groupNumberEndIndex = parts.first().indexOfFirst { !it.isDigit() }
        val groupNumber = parts.first().substring(0, groupNumberEndIndex).toInt()

        return Triple(name, groupNumber, letter) to parts[1]
    }

    private fun parseLineAsGroup(
        parts: List<String>,
        dotIndex: Int,
    ): Pair<Triple<String, Int, String?>, String> {
        val name =
            parts.first()
                .substring(dotIndex + 1)
                .lowercase()
                .replace("group", "")
                .replace("(cont)", "")
                .trim()

        val groupNumberEndIndex = parts.first().indexOfFirst { !it.isDigit() }
        val groupNumber = parts.first().substring(0, groupNumberEndIndex).toInt()

        return Triple(name, groupNumber, null) to parts[1]
    }

    private fun parseLine(line: String): Pair<Triple<String, Int, String?>, String> {
        val parts =
            line.split("\t").map {
                it.removePrefix("\"").removeSuffix("\"").trim()
            }
        val dotIndex = parts.first().indexOfLast { it == '.' }
        return if (parts.first()[dotIndex - 1].isLetter()) {
            parseLineAsSubgroup(parts, dotIndex)
        } else {
            parseLineAsGroup(parts, dotIndex)
        }
    }

    private fun saveToEntity(value: Pair<Triple<String, Int, String?>, String>): CropGroupEntity {
        val name = value.first.first
        val groupNumber = value.first.second
        val letter = value.first.third
        val group = cropGroupRepository.save(CropGroupEntity(letter ?: "", groupNumber, name))
        val entries =
            value.second
                .replace(Regex("\\(.*\\)"), "")
                .split(";")
                .filter { !it.contains("cultivars") && !it.contains("varieties") && !it.contains("hybrids") }
                .map {
                    cropGroupEntryRepository.save(
                        CropGroupEntry(
                            it.lowercase()
                                .trim()
                                .split(",")
                                .map { it.trim() }
                                .reversed()
                                .joinToString(" "),
                            group,
                        ),
                    )
                }
        group.entries.addAll(entries)
        return group
    }

    private fun writeDataToDatabase(inputFile: File) {
        val groups: MutableList<Pair<Triple<String, Int, String?>, String>> = mutableListOf()
        inputFile.forEachLine {
            val newGroup = parseLine(it)
            val existingIndex =
                groups.indexOfFirst {
                    it.first == newGroup.first
                }
            if (existingIndex == -1) {
                groups.add(newGroup)
            } else {
                groups[existingIndex] = newGroup.first to groups[existingIndex].second + " " + newGroup.second
            }
        }
        groups.map(::saveToEntity)
    }
}
