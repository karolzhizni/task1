class Stats(routineStatus: Int, money: Int, energy: Int, points: Int) {
    var money: Int = money
        private set
    var energy: Int = energy
        private set
    var points: Int = points
        private set
    fun showResources()
    {println("Энергия: $energy/10 Бюджет: $money Очков: $points")}
    fun Change(DeltaEnergy: Int, DeltaMoney: Int, deltaPoint: Int): Boolean {
        if (TransactionApprove(DeltaEnergy, DeltaMoney, deltaPoint)){
            money += DeltaMoney
            points += deltaPoint
            energy = minOf(energy + DeltaEnergy, 10)
            return true
        }
        return false
    }
    fun TransactionApprove(DeltaEnergy: Int, DeltaMoney: Int, DeltaPoint: Int): Boolean {
        if (DeltaEnergy + energy >= 0 && DeltaMoney + money >= 0) {return true
        }
        return false
    }
    var routineStatus: Int = 0
        private set
    fun setRoutineStatus(status: Int){
        if (status in 0..2){routineStatus = status}
    }
}