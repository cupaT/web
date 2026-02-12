import org.assertj.core.api.Assertions

val baseUrl: String by env
val greetingPath: String by env
val testName: String by env
val testSurname: String by env
val nonExistingId: String by env

val greetingUrl = "$baseUrl/$greetingPath"

// 1. GET /greeting без параметров -> GreetingMain
GET(greetingUrl) {
    accept("application/json")
} then {
    Assertions.assertThat(code)
        .`as`("GET /greeting без id должен вернуть 200")
        .isEqualTo(200)

    val text = jsonPath().readString("$.text")
    Assertions.assertThat(text)
        .`as`("Поле text в GreetingMain не должно быть пустым")
        .isNotBlank()
}

// 2. POST /greeting -> создать пользователя, проверить ответ и сохранить id
val createdUserId by POST(greetingUrl) {
    header("Content-Type", "application/json")
    accept("application/json")

    body(
        """
        {
          "name": "$testName",
          "surname": "$testSurname"
        }
        """.trimIndent()
    )
} then {
    Assertions.assertThat(code)
        .`as`("POST /greeting должен вернуть 200")
        .isEqualTo(200)

    val text = jsonPath().readString("$.text")
    Assertions.assertThat(text)
        .`as`("GreetingUser.text должен содержать имя и фамилию")
        .contains(testName)
        .contains(testSurname)

    val id = jsonPath().readString("$.id")
    Assertions.assertThat(id)
        .`as`("id из GreetingUser должен быть непустым UUID")
        .isNotBlank()

    // Вернём id в переменную createdUserId
    id
}

// 3. GET /greeting?id={id} -> вернуть UserData
GET(greetingUrl) {
    accept("application/json")
    queryParam("id", createdUserId)
} then {
    Assertions.assertThat(code)
        .`as`("GET /greeting?id=... должен вернуть 200 для существующего пользователя")
        .isEqualTo(200)

    val name = jsonPath().readString("$.name")
    val surname = jsonPath().readString("$.surname")

    Assertions.assertThat(name)
        .`as`("UserData.name должен совпадать с отправленным")
        .isEqualTo(testName)
    Assertions.assertThat(surname)
        .`as`("UserData.surname должен совпадать с отправленным")
        .isEqualTo(testSurname)
}

// 4. GET /greeting/{id} -> вернуть UserData
GET("$greetingUrl/$createdUserId") {
    accept("application/json")
} then {
    Assertions.assertThat(code)
        .`as`("GET /greeting/{id} должен вернуть 200 для существующего пользователя")
        .isEqualTo(200)

    val name = jsonPath().readString("$.name")
    val surname = jsonPath().readString("$.surname")

    Assertions.assertThat(name)
        .`as`("UserData.name должен совпадать с отправленным")
        .isEqualTo(testName)
    Assertions.assertThat(surname)
        .`as`("UserData.surname должен совпадать с отправленным")
        .isEqualTo(testSurname)
}

// 5. Негативные кейсы: несуществующий id -> 404

// 5.1 GET /greeting?id=nonExistingId
GET(greetingUrl) {
    accept("application/json")
    queryParam("id", nonExistingId)
} then {
    Assertions.assertThat(code)
        .`as`("GET /greeting?id=nonExistingId должен вернуть 404")
        .isEqualTo(404)
}

// 5.2 GET /greeting/{nonExistingId}
GET("$greetingUrl/$nonExistingId") {
    accept("application/json")
} then {
    Assertions.assertThat(code)
        .`as`("GET /greeting/{nonExistingId} должен вернуть 404")
        .isEqualTo(404)
}
