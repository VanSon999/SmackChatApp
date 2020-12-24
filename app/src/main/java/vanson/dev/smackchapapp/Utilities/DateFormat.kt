package vanson.dev.smackchapapp.Utilities

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
}