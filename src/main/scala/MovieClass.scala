import sun.misc.FloatingDecimal

/**
 *
 * @param title
 * @param genre
 * @param lead_studio
 * @param audience_score
 * @param profitability
 * @param rotten_tomatoes_score
 * @param worldwide_gross
 * @param year
 */
final case class MovieClass(title: String,
                            genre:String,
                            lead_studio:String,
                            audience_score :Int,
                            profitability: String,
                            rotten_tomatoes_score: Int,
                            worldwide_gross: String,
                            year: Int)
