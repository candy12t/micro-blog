package controllers

import scalikejdbc._
import models.Comment

object CommentRepository {
  def comment_list(post_id: String): Seq[Comment] = DB readOnly { implicit sesssion =>
    sql"""SELECT id, user_id, text, parent_post_id, comment_count, posted_at
          FROM posts
          WHERE parent_post_id = ${post_id}
          ORDER by posted_at DESC
       """
      .map { rs =>
        Comment(
          rs.string("id"),
          rs.string("user_id"),
          rs.string("text"),
          rs.string("parent_post_id"),
          rs.int("comment_count"),
          rs.offsetDateTime("posted_at")
        )
      }
      .list()
      .apply()
  }

  def comment_add(comment: Comment): Unit = DB localTx { implicit session =>
    sql"""INSERT INTO posts (id, user_id, text, parent_post_id, comment_count, posted_at)
          VALUES (${comment.id}, ${comment.user_id}, ${comment.text}, ${comment.parent_post_id}, ${comment.comment_count}, ${comment.posted_at})
       """
      .update()
      .apply()
  }

  def comment_count(post_id: String): Unit = DB localTx { implicit session =>
    sql"""UPDATE posts
          SET comment_count = comment_count + 1
          WHERE id = ${post_id}
       """
      .update()
      .apply()
  }
}
