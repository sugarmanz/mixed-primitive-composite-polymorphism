import kotlinx.serialization.json.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private const val name = "ISC"
private const val url = "https://opensource.org/licenses/ISC"

internal class LicenseTest {
    @Test fun `decode primitive`() {
        val license: License = Json.decodeFromJsonElement(JsonPrimitive(name))
        assertIs<License.Primitive>(license)
        assertEquals(name, license.license)
    }

    @Test fun `decode object`() {
        val license: License = Json.decodeFromJsonElement(buildJsonObject {
            put("name", name)
            put("url", url)
        })
        assertIs<License.Boxed>(license)
        assertEquals(name, license.name)
        assertEquals(url, license.url)
    }

    @Test fun `collection of primitives`() {
        val licenses: List<License> = Json.decodeFromJsonElement(buildJsonArray {
            add(name)
            add("$name-2")
        })
        val (license, license2) = licenses

        assertIs<License.Primitive>(license)
        assertEquals(name, license.license)

        assertIs<License.Primitive>(license2)
        assertEquals("$name-2", license2.license)
    }

    @Test fun `collection of objects`() {
        val licenses: List<License> = Json.decodeFromJsonElement(buildJsonArray {
            add(buildJsonObject {
                put("name", name)
                put("url", url)
            })
            add(buildJsonObject {
                put("name", "$name-2")
                put("url", "$url-2")
            })
        })
        val (license, license2) = licenses

        assertIs<License.Boxed>(license)
        assertEquals(name, license.name)
        assertEquals(url, license.url)

        assertIs<License.Boxed>(license2)
        assertEquals("$name-2", license2.name)
        assertEquals("$url-2", license2.url)
    }

    @Test fun `collection of primitive and objects`() {
        val licenses: List<License> = Json.decodeFromJsonElement(buildJsonArray {
            add(name)
            add(buildJsonObject {
                put("name", "$name-2")
                put("url", "$url-2")
            })
        })
        val (license, license2) = licenses

        assertIs<License.Primitive>(license)
        assertEquals(name, license.license)

        assertIs<License.Boxed>(license2)
        assertEquals("$name-2", license2.name)
        assertEquals("$url-2", license2.url)
    }
}
