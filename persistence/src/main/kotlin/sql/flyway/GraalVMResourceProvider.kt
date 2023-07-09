package sql.flyway

import org.flywaydb.core.api.Location
import org.flywaydb.core.api.ResourceProvider
import org.flywaydb.core.api.resource.LoadableResource
import org.flywaydb.core.internal.resource.classpath.ClassPathResource
import java.io.IOException
import java.io.UncheckedIOException
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.collections.ArrayList
import kotlin.collections.Collection
import kotlin.collections.MutableList


internal class GraalVMResourceProvider(private val locations: Array<Location>) : ResourceProvider {
    override fun getResource(name: String): ClassPathResource? {
        return if (classLoader.getResource(name) == null) {
            null
        } else {
            ClassPathResource(null, name, classLoader, StandardCharsets.UTF_8)
        }
    }

    override fun getResources(prefix: String, suffixes: Array<String>): Collection<LoadableResource> = try {
        FileSystems.newFileSystem(URI.create("resource:/"), mapOf<String, Any?>()).use { fileSystem ->
            val result: MutableList<LoadableResource> = ArrayList()
            for (location in locations) {
                val path = fileSystem.getPath(location.path)
                Files.walk(path).use { files ->
                    files
                        .filter { path: Path? -> Files.isRegularFile(path!!) }
                        .filter { file: Path -> file.fileName.toString().startsWith(prefix) }
                        .filter { file: Path -> hasSuffix(file.fileName.toString(), suffixes) }
                        .map { file: Path -> ClassPathResource(null, file.toString(), classLoader, StandardCharsets.UTF_8) }
                        .forEach { e: LoadableResource -> result.add(e) }
                }
            }
            return result
        }
    } catch (ex: IOException) {
        throw UncheckedIOException(ex)
    }

    companion object {
        private fun hasSuffix(input: String, suffixes: Array<String>): Boolean {
            for (suffix in suffixes) {
                if (input.endsWith(suffix)) {
                    return true
                }
            }
            return false
        }

        private val classLoader: ClassLoader
            get() = GraalVMResourceProvider::class.java.classLoader
    }
}