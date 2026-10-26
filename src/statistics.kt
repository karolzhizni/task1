class Stats(money: Int, energy: Int, points: Int) {
    var lastAction: Action? = null
        private set
    var money: Int = money
        private set
    var energy: Int = energy
        private set
    var points: Int = points
        private set
    fun showResources()
    {println("Энергия: $energy/10 Бюджет: $money Очков: $points")}
    fun change(deltaEnergy: Int, deltaMoney: Int, deltaPoint: Int): Boolean {
        if (transactionApprove(deltaEnergy, deltaMoney)){
            money += deltaMoney
            points += deltaPoint
            energy = minOf(energy + deltaEnergy, 10)
            return true
        }
        return false
    }
    fun transactionApprove(deltaEnergy: Int, deltaMoney: Int): Boolean {
        return deltaEnergy + energy > 0 && deltaMoney + money > 0
    }
    fun pointsFor(a: Action): Int {
        return if (a == lastAction) a.points / 2 else a.points
    }
    fun changeByAction(a: Action): Boolean {
        if (!change(a.energy, a.money, pointsFor(a))) return false
        lastAction = a
        return true
    }
}



fun signed(n: Int) = if (n > 0) "+$n" else "$n"

data class Log(val dayLeast: Int, val action: String, val dEnergy: Int, val dMoney: Int, val dPoint: Int, val eventData: Event,
               val eventDEnergy: Int, val eventDMoney: Int, val eventDPoint: Int, val globalDeltaPoints: Int){
    fun show(){
        println("${8 - dayLeast} день: $action (энергия ${signed(dEnergy)}, деньги ${signed(dMoney)}, очки ${signed(dPoint)}), событие: ${eventData.title} (энергия ${signed(eventDEnergy)}, деньги ${signed(eventDMoney)}, очки ${signed(eventDPoint)}). Итого за день: ${signed(globalDeltaPoints)} очков")
    }
}