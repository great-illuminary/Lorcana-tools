package eu.codlab.lorcana.blipya.licenses.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LicenseSubLicense(
    val license: String? = "",
    @SerialName("license_url")
    val licenseUrl: String? = ""
)

@Serializable
data class LicenseProject(
    val uniqueId: String = "",
    val artifactVersion: String = "",
    val description: String? = "",
    val name: String = "",
    val website: String = "",
    val licenses: List<String> = emptyList(),
) {
    companion object {
        fun fromArray(raw: List<LicenseProject>): List<LicenseProject> {
            val map: MutableMap<String, LicenseProject> = mutableMapOf()

            raw.forEach { licenseProject ->
                val uniqueId = licenseProject.uniqueId
                val version = licenseProject.artifactVersion

                val newVersion = versionFromMavenDependency(version)

                val split = uniqueId.splitToSequence(":")
                val left = split.take(split.count() - 1).joinToString(":")

                val inMap = map[left]

                if (null == inMap) {
                    map[left] = licenseProject
                } else {
                    val originalVersion = versionFromMavenDependency(inMap.artifactVersion)
                    if (newVersion.isAtLeast(
                            originalVersion.major,
                            originalVersion.minor,
                            originalVersion.patch,
                            originalVersion.sub
                        )
                    ) {
                        map[left] = licenseProject
                    }
                }
            }
            map.values.forEach { println(it.uniqueId) }
            return map.values.toList()
        }
    }
}

@Suppress("MagicNumber")
private fun versionFromMavenDependency(mavenDependency: String): KotlinVersion {
    val split = mavenDependency.split(":")

    val mavenVersion = split.last().split("-").first()
    val versionSplit = mavenVersion.split(".")
    println("managing version $mavenVersion")
    return when (versionSplit.count()) {
        1 -> KotlinVersion(
            versionSplit[0].split("_").first().split("-").first().toInt(),
            0
        )

        2 -> KotlinVersion(
            versionSplit[0].split("_").first().split("-").first().toInt(),
            versionSplit[1].split("_").first().split("-").first().toInt()
        )

        3 -> KotlinVersion(
            versionSplit[0].split("_").first().split("-").first().toInt(),
            versionSplit[1].split("_").first().split("-").first().toInt(),
            versionSplit[2].split("_").first().split("-").first().toInt()
        )

        4 -> KotlinVersion(
            versionSplit[0].split("_").first().split("-").first().toInt(),
            versionSplit[1].split("_").first().split("-").first().toInt(),
            versionSplit[2].split("_").first().split("-").first().toInt(),
            versionSplit[3].split("_").first().split("-").first().toIntOrNull()
        )

        else -> throw IllegalStateException("unknown for $mavenVersion with original $mavenDependency")
    }
}

class KotlinVersion(val major: Int, val minor: Int, val patch: Int, val sub: Int? = null) :
    Comparable<KotlinVersion> {
    /**
     * Creates a version from [major] and [minor] components, leaving [patch] component zero.
     */
    constructor(major: Int, minor: Int) : this(major, minor, 0)

    private val version = versionOf(major, minor, patch)

    @Suppress("MagicNumber")
    private fun versionOf(major: Int, minor: Int, patch: Int): Int {
        require(major in 0..Int.MAX_VALUE && minor in 0..Int.MAX_VALUE && patch in 0..Int.MAX_VALUE) {
            "Version components are out of range: $major.$minor.$patch"
        }
        return major.shl(16) + minor.shl(8) + patch
    }

    /**
     * Returns the string representation of this version
     */
    override fun toString(): String = "$major.$minor.$patch"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherVersion = (other as? KotlinVersion) ?: return false
        return this.version == otherVersion.version
    }

    override fun hashCode(): Int = version

    override fun compareTo(other: KotlinVersion): Int = version - other.version

    /**
     * Returns `true` if this version is not less than the version specified
     * with the provided [major] and [minor] components.
     */
    fun isAtLeast(major: Int, minor: Int): Boolean =
        // this.version >= versionOf(major, minor, 0)
        this.major > major || (
                this.major == major &&
                        this.minor >= minor
                )

    /**
     * Returns `true` if this version is not less than the version specified
     * with the provided [major], [minor] and [patch] components.
     */
    fun isAtLeast(major: Int, minor: Int, patch: Int): Boolean =
        // this.version >= versionOf(major, minor, patch)
        this.major > major || (
                this.major == major && (
                        this.minor > minor || (
                                this.minor == minor &&
                                        this.patch >= patch
                                )
                        )
                )

    /**
     * Returns `true` if this version is not less than the version specified
     * with the provided [major], [minor] and [patch] components.
     */
    @Suppress("ReturnCount")
    fun isAtLeast(major: Int, minor: Int, patch: Int, sub: Int? = 0): Boolean {
        listOf(
            this.major to major,
            this.minor to minor,
            this.patch to patch,
            (this.sub ?: 0) to (sub ?: 0)
        ).forEach { (V, v) ->
            if (V < v) return false
            if (V > v) return true
        }
        return false
    }
}
