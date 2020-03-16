package models

import play.api.libs.json._
import java.time.OffsetDateTime

case class Post(id: String,
                user_id: String,
                text: String,
                parent_post_id: String,
                comment_count: Long,
                posted_at: OffsetDateTime)

object Post {

  implicit val writes: Writes[Post] = Json.writes[Post]

  def apply(id: String, user_id: String, text: String, posted_at: OffsetDateTime): Post =
    Post(id, user_id, text, null, 0, posted_at)
}
