package tutorial.model


case class Trade(
  transactionType: String,
  status: String = "Finished",
  id: String,
  tid: String,
  v: Int,
  mid: Option[String] = None,
  details: Map[String, AnyRef] = Map.empty
);
