package nl.dorost.flow.core

class Container(
    var id: String? = null,
    val name: String? = null,
    var type: String? = null,
    var firstBlock: String? = null,
    var lastBlock: String? = null,
    var description: String? = null,
    var params: List<String>? = null,
    var update: Boolean = false
)