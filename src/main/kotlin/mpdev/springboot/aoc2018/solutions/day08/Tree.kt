package mpdev.springboot.aoc2018.solutions.day08

class Tree(input: List<String>) {

    private var nodeId = 0
    val dataRoot = Node(nodeId++)

    init {
        processInput(dataRoot, input[0].split(" ").map { it.toInt() }.toMutableList())
    }

    fun calculateMetadataSum(): Int {
        var sumMetadata = 0
        traverseTree(dataRoot) { node,_ -> sumMetadata += node.metadata.sum() }
        return sumMetadata
    }

    fun calculateValue() = traverseTree2(dataRoot)

    fun traverseTree(node: Node,  level: Int = 0, apply: (Node, Int) -> Unit) {
        apply(node, level)
        node.children.forEach { child -> traverseTree(child, level+1, apply) }
    }

    private fun traverseTree2(node: Node): Int {
        return if (node.noOfChildren == 0)
            node.metadata.sum()
        else {
            var thisSum = 0
            node.metadata.forEach { i ->
                if (i in 1 .. node.noOfChildren)
                    thisSum += traverseTree2(node.children[i-1])
            }
            thisSum
        }
    }

    fun print() {
        printNode(dataRoot)
    }

    private fun printNode(node: Node) {
        traverseTree(node) { n,l -> (0..(4*l)).forEach { _ -> print(" ") }; println(n) }
    }

    private fun processInput(node: Node, input: MutableList<Int>) {
        var noOfChildren = input[0]
        node.noOfChildren = noOfChildren
        node.sizeOfMetadata = input[1]
        input.removeFirst()
        input.removeFirst()
        while (noOfChildren > 0) {
            val newChild = Node(nodeId++)
            node.children.add(newChild)
            processInput(newChild, input)
            --noOfChildren
        }
        for (i in 0 until node.sizeOfMetadata) {
            node.metadata.add(input.first())
            input.removeFirst()
        }
    }
}

data class Node(val id: Int, var noOfChildren: Int = 0, var sizeOfMetadata: Int = 0) {
    var children: MutableList<Node> = mutableListOf()
    var metadata: MutableList<Int> = mutableListOf()
    override fun toString() = "id: $id metadata: $sizeOfMetadata = $metadata, number of children: $noOfChildren"
}