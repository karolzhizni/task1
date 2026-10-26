abstract class Event(
    val title: String,
    val about: String,
    protected val dEnergy: Int,
    protected val dMoney: Int,
    protected val dPoints: Int,
) {
    abstract fun apply(stats: Stats) : String
    abstract fun giveLog() : String
}
class FixedEvent(
    title: String,
    about: String,
    dEnergy: Int,
    dMoney: Int,
    dPoints: Int,
) : Event(title, about, dEnergy, dMoney, dPoints) {

    override fun apply(stats: Stats): String {
        if (stats.change(dEnergy, dMoney, dPoints)) return giveLog()
        return "$title. $about\nОбошлось без последствий."
    }
    override fun giveLog(): String {return "$title: $about\nЭнергия ${if (dEnergy>0) "+" else ""}$dEnergy, деньги ${if (dMoney > 0) "+" else ""}$dMoney, очки ${if (dPoints > 0) "+" else ""}$dPoints"}
}


class CondEvent(
    title: String,
    about: String,
    dEnergy: Int,
    dMoney: Int,
    dPoints: Int,
    private val reqEnergy: Int,
    private val reqMoney: Int,
    private val reqPoints: Int,
    private val maxEnergy: Int = 10,   // верхняя граница энергии; по умолчанию 10, то есть ограничения нет
) : Event(title, about, dEnergy, dMoney, dPoints) {
    override fun apply(stats: Stats): String {
        if (stats.energy in reqEnergy..maxEnergy && reqMoney <= stats.money && reqPoints <= stats.points) {
            if (stats.change(dEnergy, dMoney, dPoints)) return giveLog()
            return "$title. $about\nОбошлось без последствий."
        }
        else {return "$title: без изменений"}
    }
    override fun giveLog(): String {return "$title: $about\nЭнергия ${if (dEnergy>0) "+" else ""}$dEnergy, деньги ${if (dMoney > 0) "+" else ""}$dMoney, очки ${if (dPoints > 0) "+" else ""}$dPoints"}
}

