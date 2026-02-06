fun main(args: Array<String>) {
    val input = args.joinToString(" ")

    val words = input.split(" ")

    val counts = words.groupingBy { it }.eachCount()

    for (word in words.distinct().sortedWith((
            compareByDescending<String> { word -> counts[word] ?: 0 }
                .thenBy { word -> word }
            ))) {
        val count = counts[word] ?: 0
        println("$word $count")
    }
}