package tutorial.model

sealed trait TradeStatus
object TradeStatus {
  case object None extends TradeStatus
  case object Finished extends TradeStatus
  case class Failed(reason: String) extends TradeStatus
  case class Discarded(reason: String) extends TradeStatus
  case object Captured extends TradeStatus
  case object Processing extends TradeStatus
}

sealed trait TransactionType
object TransactionType {
  case object NEW extends TransactionType
  case object AMEND extends TransactionType
  case object CANCEL extends TransactionType
}

case class Trade(transcationType: TransactionType,
                 status: TradeStatus = TradeStatus.None,
                 tid: String, v: Int,
                 mid: Option[String] = None);
