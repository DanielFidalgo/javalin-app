import infrastructure.Application
import io.agroal.pool.ConnectionHandler
import java.util.logging.Level
import java.util.logging.Logger

lateinit var application: Application
fun main() {
    Logger.getAnonymousLogger().log(Level.INFO, (Array<ConnectionHandler>::class.java).name)
    application = Application()
}