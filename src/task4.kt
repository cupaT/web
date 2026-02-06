fun main(args: Array<String>) {
    val input = args.joinToString(" ")

    val words = input.split(" ")

    val counts = words.groupingBy { it }.eachCount()

    for (word in words.sorted().distinct()) {
        val count = counts[word] ?: 0
        println("$word $count")
    }
}