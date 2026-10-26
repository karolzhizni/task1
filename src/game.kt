enum class Action(val title: String, val energy: Int, val money: Int, val points: Int) {
    WORK("Работа", -4, 0, 18),
    INVEST("Вложение", 0, -30, 22),
    REST("Отдых", 4, 0, 0),
}

class Game(var totalDays: Int, var stats: Stats) {
    val eventsList = listOf<Event>(
        FixedEvent("Мелочь, а приятно", "вы нашли на асфальте немного монет.", 0, 15, 0),
        CondEvent("Не с той ноги", "вы встали не с той ноги, и весь день всё валится из рук.", -2, 0, 0, 3, 0, 5),
        CondEvent("Вандалы","вандалы осквернили ваш алтарь, придется его восстанавливать", 0,0,-10,0,0,11),
        CondEvent("Энергетики","в магазине продавались энергетики по большой скидке. Вы не могли отказаться от такого заманчивого предложения", 4,-10,0,0,11,0,6),
        FixedEvent("Благословение", "вы ощущаете благосклонность к вам со стороны небесных сил",0,0,10),
        CondEvent("Дырявый карман", "карман вашей куртки оказался дырявым. Из него выпала часть денег.", 0,-15,0,0,16,0),
        FixedEvent("Большая удача", "вы нашли на асфальте купюру большого номинала.",0,30,0),
        FixedEvent("Вы не одни", "кто-то помимо вас оставил подношение и улучшил алтарь.", 0,0,15)
    )
    val logList = mutableListOf<Log>()
    val art = Arts()
    fun about(){
        println("""
            Вы просыпаетесь в холодном поту. Вам снилось, что через 7 дней на землю упадет гигантский метеорит и окончит историю человеческой цивилизации, если не
            воззвать к божеству [ДАННЫЕ УДАЛЕНЫ]. Читая новости, вы понимаете, что это был не сон. Вы решаете спасти человечество.

            ПРАВИЛА
            У вас ${stats.money} денег, ${stats.energy}/10 энергии и ${stats.points} очков подготовки.
            Цель: набрать 100 очков подготовки за $totalDays дней. Не успеете — метеорит упадёт.

            Каждый день вы выбираете одно действие:
              1 - Улучшить алтарь: ${Action.WORK.energy} энергии, +${Action.WORK.points} подготовки.
              2 - Принести подношения: ${Action.INVEST.money} денег, +${Action.INVEST.points} подготовки.
              3 - Отдых: +${Action.REST.energy} энергии (больше 10 не накопить).
            Каждый день с вами что-нибудь случается: событие может добавить или отнять ресурсы.

            Особые правила этого мира:
              - Повторять вчерашнее дело скучно: если сделать то же, что вчера, очков подготовки будет вдвое меньше.
                Такое действие отмечено в меню звёздочкой (*).
              - Довести себя до нуля нельзя: действие, после которого энергия или деньги кончатся, выполнить не получится.

            Нажмите Enter.
        """.trimIndent())
        readlnOrNull()
    }
    fun win(){
        println("""Божество заметило вас. Метеорит развернулся в другом направлении. Человечество спасено.
            |
            |Нажмите Enter.
        """.trimMargin())
        art.showWin()
        readlnOrNull()
    }
    fun lose(){
        println("""Божество проигнорировало ваши мольбы. Вы наблюдаете, как метеорит стирает человечество с лица Земли. В следующее мгновение вы оказываетесь у себя дома за семь дней до...
            |
            |Попробуйте снова. Нажмите Enter.
        """.trimMargin())
        art.showLose()
        readlnOrNull()
    }
    fun finalLogs(){
        println("Ваши действия: ")
        for (log in logList){
            log.show()
        }

        val best = logList.maxBy { it.globalDeltaPoints }
        val worst = logList.minBy { it.globalDeltaPoints }
        println("Лучший день: ${8 - best.dayLeast} (${signed(best.globalDeltaPoints)} очков)")
        println("Худший день: ${8 - worst.dayLeast} (${signed(worst.globalDeltaPoints)} очков)")
        val energySpent = logList.filter { it.dEnergy < 0 }.sumOf { -it.dEnergy }
        val moneySpent = logList.filter { it.dMoney < 0 }.sumOf { -it.dMoney }
        println("Всего потрачено на действия: энергии $energySpent, бюджета $moneySpent")
        println("Труд: ${logList.count { it.action == "Труд" }}, подношение: ${logList.count { it.action == "Подношение" }}, отдых: ${logList.count { it.action == "Отдых" }}")
    }
    fun run(){
        about()
        while (totalDays > 0){
            val energyAtDayStart = stats.energy
            val moneyAtDayStart = stats.money
            val pointsAtDayStart = stats.points
            val curEvents = eventsList.random()
            art.showApproach(totalDays)
            println(curEvents.apply(stats))
            stats.showResources()
            println("""Что вы хотите сделать?
1 - Улучшить алтарь: ${Action.WORK.energy} энергии, +${stats.pointsFor(Action.WORK)}${routineMark(Action.WORK)} подготовки.
2 - Принести подношения: ${Action.INVEST.money} денег, +${stats.pointsFor(Action.INVEST)}${routineMark(Action.INVEST)} подготовки.
3 - Отдых: +${Action.REST.energy} энергии.
            """.trimIndent())
            if (stats.lastAction == Action.WORK || stats.lastAction == Action.INVEST) {
                println("* - Вам неинтересно заниматься тем же, что вы делали вчера. Получаемые очки подготовки снижены вдвое.")
            }
            chooseAction(curEvents, energyAtDayStart, moneyAtDayStart, pointsAtDayStart)
            if (stats.points >= 100){win(); break}
            totalDays--
            println("Осталось $totalDays дней...")



        }
        if (totalDays == 0){lose()}
        finalLogs()
    }
    // звёздочка у действия, которое повторяет вчерашнее и даёт вдвое меньше очков
    fun routineMark(action: Action): String = if (stats.lastAction == action) " (*)" else ""

    fun doAction(action: Action): Boolean = stats.changeByAction(action)

    fun chooseAction(curEvent: Event, energyAtDayStart: Int, moneyAtDayStart: Int, pointsAtDayStart: Int){
        val energyBefore = stats.energy
        val moneyBefore = stats.money
        val pointsBefore = stats.points
        var actionTitle = ""

        var nonApprovedTransaction = true
        while (nonApprovedTransaction){
            val playerChoose = readln().toIntOrNull()
            if (playerChoose in 1..3){
                if (playerChoose == 1){
                    if (doAction(Action.WORK)) {nonApprovedTransaction = false; actionTitle = "Труд"
                    } else {println("Вы слишком устали и не можете трудиться.")}
                }
                if (playerChoose == 2){
                    if (doAction(Action.INVEST)) {nonApprovedTransaction = false; actionTitle = "Подношение"} else {println("У вас нет денег и вы не можете принести подношение.")}
                }
                if (playerChoose == 3){doAction(Action.REST)
                nonApprovedTransaction = false; actionTitle = "Отдых"}
            }
            else{
                println("Некорректный ввод. Попробуйте снова.")
            }
        }
        logList.add(Log(totalDays, actionTitle, stats.energy - energyBefore, stats.money - moneyBefore,
            stats.points - pointsBefore, curEvent,
            energyBefore - energyAtDayStart, moneyBefore - moneyAtDayStart, pointsBefore - pointsAtDayStart,
            stats.points - pointsAtDayStart))

    }
}