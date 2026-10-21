fun work(stats: Stats): Boolean {
    var koef = 1
    if (stats.routineStatus == 1){koef = 2}
     if (stats.Change(-3, 0, 16/koef)){return true}
    return false
}

fun invest(stats: Stats): Boolean {
    var koef = 1
    if (stats.routineStatus == 2){koef = 2}
    if (stats.Change(0, -30, 20/koef)){return true}
    return false

}

fun rest(stats: Stats): Boolean {
    if(stats.Change(4, 0, 0)){return true}
    return false
}

// дописать запись в логи действий
class Game(var totalDays: Int, var stats: Stats) {
    fun about(){
        println("чето там")
    }
    fun win(){
        println("ГОООЛ ПОБЕДААА")
    }
    fun lose(){
        println("позор...")
    }
    fun finalLogs(){
        println("Ваши действия: ")
        TODO()
    }
    fun run(){
        about()
        while (totalDays > 0){
            stats.showResources()
            // место под ивенты


            //
            println("""Что вы хотите сделать?
1 - Работать: -3 энергии, +${if (stats.routineStatus == 1){"8(*)"} else 16} подготовки.
2 - Вложиться: -30 денег, +${if (stats.routineStatus == 2) {"10(*)"} else 20} подготовки.
3 - Отдых: +4 энергии. ${if (stats.routineStatus != 0){"\n* - Вам неинтересно заниматься тем же, что вы делали вчера. Получаемые очки подготовки снижены вдвое." } else ""}
            """.trimIndent())
            doAction()
            if (stats.points >= 100){win(); break}
            totalDays--
            println("Осталось $totalDays дней...")
            // дописать лог в doAction



        }
        if (totalDays == 0){lose()}

    }
    fun doAction(){
        var nonApprovedTransaction = true
        while (nonApprovedTransaction){
            val playerChoose = readln().toIntOrNull()
            if (playerChoose in 1..3){
                if (playerChoose == 1){
                    if (work(stats)) {nonApprovedTransaction = false; stats.setRoutineStatus(1)} else {println("Вы слишком устали и не можете работать.")}
                }
                if (playerChoose == 2){
                    if (invest(stats)) {nonApprovedTransaction = false; stats.setRoutineStatus(2)} else {println("У вас нет денег и вы не можете вложиться.")}
                }
                if (playerChoose == 3){rest(stats)
                nonApprovedTransaction = false; stats.setRoutineStatus(0)}
            }
            else{
                println("Некорректный ввод. Попробуйте снова.")
            }
        }

    }
}