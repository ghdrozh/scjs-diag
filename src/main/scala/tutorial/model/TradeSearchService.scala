package tutorial.model

object TradeSearchService {

  def find(id: String): Seq[Trade] = {
    Seq(
      Trade(TransactionType.NEW, TradeStatus.Finished, "tid1", 1),
      Trade(TransactionType.NEW, TradeStatus.Finished, "tid2", 1, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "tid2", 2, Some("tid1")),
      Trade(TransactionType.CANCEL, TradeStatus.Finished, "tid2", 3, Some("tid1")),
      Trade(TransactionType.NEW, TradeStatus.Finished, "tid3", 1, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "tid3", 2, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "tid3", 3, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "tid3", 4, Some("tid1"))
    )
  }

}
