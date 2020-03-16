package models

import play.api.libs.json._
import java.time.OffsetDateTime

case class Comment(id: String,
                   user_id: String,
                   text: String,
                   parent_post_id: String,
                   comment_count: Long,
                   posted_at: OffsetDateTime)

object Comment {

  implicit val writes: Writes[Comment] = Json.writes[Comment]

  def apply(id: String,
            user_id: String,
            text: String,
            parent_post_id: String,
            posted_at: OffsetDateTime): Comment =
    Comment(id, user_id, text, parent_post_id, 0, posted_at)
}
