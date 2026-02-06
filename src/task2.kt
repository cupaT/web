fun main(args: Array<String>) {
    val input = args.joinToString(" ")

    val words = input.split(" ")

    for (word in words.sorted()) {
        println(word)
    }
}