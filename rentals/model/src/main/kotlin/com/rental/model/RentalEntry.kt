package com.rental.model

/**
 * A minimal representation of a rental entry
 *
 */
interface RentalEntry {
    /**
     * Identifier of the entry
     */
    val id: Long

    /**
     * Human-readable name of the entry
     */
    val name: String

    /**
     * HTTP URL of an image, optional
     */
    val imageUrl: String?
}