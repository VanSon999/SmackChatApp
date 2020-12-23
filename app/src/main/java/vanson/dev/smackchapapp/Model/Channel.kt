package vanson.dev.smackchapapp.Model

class Channel(val name: String, val description: String, id: String) {
    override fun toString(): String { //to print name channel when use default adapter
        return "#$name"
    }
}