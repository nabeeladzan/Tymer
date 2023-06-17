package org.finalware.tymer.model

data class Bin(
    val record: List<Preset>,
    val metadata: Metadata
)

data class Metadata(
    val id: String,
    val private: Boolean,
    val createdAt: String
)